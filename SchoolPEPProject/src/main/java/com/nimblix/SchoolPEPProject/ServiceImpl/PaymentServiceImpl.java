package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Exception.PaymentException;
import com.nimblix.SchoolPEPProject.Model.PendingPayment;
import com.nimblix.SchoolPEPProject.Model.School;
import com.nimblix.SchoolPEPProject.Model.SchoolSubscription;
import com.nimblix.SchoolPEPProject.Repository.PendingPaymentRepository;
import com.nimblix.SchoolPEPProject.Repository.SchoolRepository;
import com.nimblix.SchoolPEPProject.Repository.SchoolSubscriptionRepository;
import com.nimblix.SchoolPEPProject.Request.CreatePaymentIntentRequest;
import com.nimblix.SchoolPEPProject.Response.PaymentIntentResponse;
import com.nimblix.SchoolPEPProject.Service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PendingPaymentRepository pendingPaymentRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolSubscriptionRepository schoolSubscriptionRepository;
    private final ObjectMapper objectMapper;

    @Value("${razorpay.key:}")
    private String razorpayKey;

    @Value("${razorpay.secret:}")
    private String razorpaySecret;

    @Value("${razorpay.webhook.secret:}")
    private String razorpayWebhookSecret;

    @Override
    @Transactional
    public PaymentIntentResponse createPaymentIntent(School school, CreatePaymentIntentRequest request) {

        if (razorpayKey == null || razorpayKey.isBlank() || razorpaySecret == null || razorpaySecret.isBlank()) {
            throw new PaymentException("Razorpay key/secret not configured");
        }

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new PaymentException("Invalid amount");
        }

        if (request.getPlanType() == null || request.getPlanType().isBlank()) {
            throw new PaymentException("planType is required");
        }

        String currency = (request.getCurrency() == null || request.getCurrency().isBlank())
                ? "inr"
                : request.getCurrency().toLowerCase();

        // Razorpay expects amount in smallest currency unit (paise)
        long amountMinor = Math.round(request.getAmount() * 100);

        // Optional: prevent multiple active subscriptions
        if (schoolSubscriptionRepository.findTopBySchoolIdAndPaymentStatusOrderByIdDesc(
                school.getSchoolId(), SchoolConstants.ACTIVE).isPresent()) {
            throw new PaymentException("Active subscription already exists");
        }

        try {
            RazorpayClient client = new RazorpayClient(razorpayKey, razorpaySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountMinor);
            orderRequest.put("currency", currency);
            orderRequest.put("receipt", "school_" + school.getSchoolId() + "_" + System.currentTimeMillis());
            orderRequest.put("payment_capture", 1);

            Order order = client.orders.create(orderRequest);
            String orderId = order.get("id");

            // Track the order so webhook can activate subscription
            PendingPayment pending = PendingPayment.builder()
                    .schoolId(school.getSchoolId())
                    .planType(request.getPlanType())
                    .amount(request.getAmount())
                    .razorpayOrderId(orderId)
                    .build();
            pendingPaymentRepository.save(pending);

            return PaymentIntentResponse.builder()
                    .keyId(razorpayKey)
                    .orderId(orderId)
                    .amount(amountMinor)
                    .currency(currency)
                    .build();

        } catch (RazorpayException e) {
            throw new PaymentException("Failed to create Razorpay order", e);
        }
    }

    @Override
    @Transactional
    public void handleStripeWebhook(String payload, String signature) {

        if (razorpayWebhookSecret == null || razorpayWebhookSecret.isBlank()) {
            throw new PaymentException("Razorpay webhook secret not configured");
        }

        if (signature == null || signature.isBlank()) {
            throw new PaymentException("Missing webhook signature");
        }

        try {
            boolean verified = Utils.verifyWebhookSignature(payload, signature, razorpayWebhookSecret);
            if (!verified) {
                throw new PaymentException("Invalid Razorpay signature");
            }
        } catch (RazorpayException e) {
            throw new PaymentException("Invalid Razorpay signature", e);
        }

        // Parse order_id and payment_id from webhook payload
        String orderId;
        String paymentId;
        try {
            JsonNode root = objectMapper.readTree(payload);
            JsonNode paymentEntity = root.path("payload").path("payment").path("entity");
            orderId = paymentEntity.path("order_id").asText(null);
            paymentId = paymentEntity.path("id").asText(null);
        } catch (Exception e) {
            throw new PaymentException("Invalid webhook payload", e);
        }

        if (orderId == null || orderId.isBlank() || paymentId == null || paymentId.isBlank()) {
            return; // ignore events without payment/order details
        }

        Optional<PendingPayment> pendingOpt = pendingPaymentRepository.findByRazorpayOrderId(orderId);
        if (pendingOpt.isEmpty()) {
            // idempotency: if we've already processed, just ignore
            return;
        }

        PendingPayment pending = pendingOpt.get();

        School school = schoolRepository.findById(pending.getSchoolId())
                .orElseThrow(() -> new PaymentException("School not found for payment"));

        // Idempotency: if subscription already activated with this paymentRef, ignore
        // (paymentRef is unique so duplicates would fail anyway)
        if (schoolSubscriptionRepository.findTopBySchoolIdAndPaymentStatusOrderByIdDesc(
                school.getSchoolId(), SchoolConstants.ACTIVE).isPresent()
                && SchoolConstants.PAID.equals(school.getSubscriptionStatus())) {
            pendingPaymentRepository.delete(pending);
            return;
        }

        SchoolSubscription subscription = SchoolSubscription.builder()
                .schoolId(school.getSchoolId())
                .planType(pending.getPlanType())
                .amount(pending.getAmount())
                .paymentRef(paymentId)
                .paymentStatus(SchoolConstants.ACTIVE)
                .startDate(com.nimblix.SchoolPEPProject.Util.SchoolUtil.nowIST())
                .endDate(
                        "MONTHLY".equalsIgnoreCase(pending.getPlanType())
                                ? com.nimblix.SchoolPEPProject.Util.SchoolUtil.plusDaysIST(30)
                                : com.nimblix.SchoolPEPProject.Util.SchoolUtil.plusDaysIST(365)
                )
                .build();

        schoolSubscriptionRepository.save(subscription);

        school.setSubscriptionStatus(SchoolConstants.PAID);
        school.setIsActive(true);
        schoolRepository.save(school);

        pendingPaymentRepository.delete(pending);
    }
}

