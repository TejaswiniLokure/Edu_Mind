package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.School;
import com.nimblix.SchoolPEPProject.Request.CreatePaymentIntentRequest;
import com.nimblix.SchoolPEPProject.Response.PaymentIntentResponse;
import com.nimblix.SchoolPEPProject.Service.PaymentService;
import com.nimblix.SchoolPEPProject.Service.SchoolService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final SchoolService schoolService;

    @PostMapping("/create-intent")
    public ResponseEntity<Map<String, Object>> createIntent(@RequestBody CreatePaymentIntentRequest request) {
        School school = schoolService.getLoggedInSchool();

        PaymentIntentResponse intent = paymentService.createPaymentIntent(school, request);

        Map<String, Object> response = new HashMap<>();
        response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
        response.put(SchoolConstants.MESSAGE, "Payment intent created successfully");
        response.put("data", intent);
        return ResponseEntity.ok(response);
    }

    /**
     * Razorpay webhook endpoint. Must be publicly accessible.
     * Razorpay sends signature in header: X-Razorpay-Signature
     */
    @PostMapping("/webhook")
    public ResponseEntity<Map<String, Object>> webhook(HttpServletRequest request,
                                                       @RequestHeader(name = "X-Razorpay-Signature", required = false) String signature)
            throws Exception {

        String payload;
        try (BufferedReader reader = request.getReader()) {
            payload = reader.lines().collect(Collectors.joining("\n"));
        }

        paymentService.handleStripeWebhook(payload, signature);

        return ResponseEntity.ok(Map.of(
                SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS,
                SchoolConstants.MESSAGE, "Webhook received"
        ));
    }
}

