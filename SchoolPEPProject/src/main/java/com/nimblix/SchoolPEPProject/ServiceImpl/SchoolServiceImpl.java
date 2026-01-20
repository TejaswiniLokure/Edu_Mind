package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Helper.MailHelper;
import com.nimblix.SchoolPEPProject.Model.School;
import com.nimblix.SchoolPEPProject.Model.SchoolEmailOtp;
import com.nimblix.SchoolPEPProject.Model.SchoolSubscription;
import com.nimblix.SchoolPEPProject.Model.User;
import com.nimblix.SchoolPEPProject.Repository.SchoolEmailOtpRepository;
import com.nimblix.SchoolPEPProject.Repository.SchoolRepository;
import com.nimblix.SchoolPEPProject.Repository.SchoolSubscriptionRepository;
//import com.nimblix.SchoolPEPProject.Repository.UserRepository;
import com.nimblix.SchoolPEPProject.Request.SchoolRegistrationRequest;
import com.nimblix.SchoolPEPProject.Request.SubscriptionRequest;
import com.nimblix.SchoolPEPProject.Response.SchoolListResponse;
import com.nimblix.SchoolPEPProject.Security.CustomUserDetails;
import com.nimblix.SchoolPEPProject.Service.SchoolService;
import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final MailHelper mailHelper;
//    private final UserRepository userRepository;
    private  final SchoolEmailOtpRepository schoolEmailOtpRepository;
    private final SchoolSubscriptionRepository schoolSubscriptionRepository;
    @Override
    public School registerSchool(SchoolRegistrationRequest request) {

        if (request.getSchoolId() == null) {
            throw new IllegalArgumentException("School ID is mandatory");
        }

        if (schoolRepository.existsById(request.getSchoolId())) {
            throw new RuntimeException("School ID already exists");
        }

        if (!request.getSchoolAddress().matches(".*\\b\\d{6}\\b.*")) {
            throw new IllegalArgumentException(
                    "School address must contain a valid 6-digit pincode"
            );
        }

        if (schoolRepository.existsBySchoolEmail(request.getSchoolEmail())) {
            throw new RuntimeException("School already registered with this email");
        }

        String locationType = "MANUAL";
        if (request.getLatitude() != null && request.getLongitude() != null) {
            locationType = "GPS";
        }

        School school = School.builder()
                .schoolId(request.getSchoolId())
                .schoolName(request.getSchoolName())
                .schoolAddress(request.getSchoolAddress())
                .schoolPhone(request.getSchoolPhone())
                .password(request.getPassword()) // hash later
                .schoolEmail(request.getSchoolEmail())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .locationType(locationType)
                .emailVerified(Boolean.FALSE)
                .status(SchoolConstants.ACTIVE)
                .build();

        schoolRepository.save(school);

        // 🔹 Generate OTP
        Integer otp = MailHelper.getSixDigitRandomNumber();

        SchoolEmailOtp emailOtp = new SchoolEmailOtp();
        emailOtp.setEmail(request.getSchoolEmail());
        emailOtp.setOtp(String.valueOf(otp));
        emailOtp.setVerified(Boolean.FALSE);

       // Set expiry time as String in IST format
        emailOtp.setExpiryTime(SchoolUtil.getExpiryTimeInISTString(5));


        schoolEmailOtpRepository.save(emailOtp);

        mailHelper.sendOtpMail(
                request.getSchoolEmail(),
                request.getSchoolName(),
                String.valueOf(otp),
                "School Registration OTP Verification" // ✅
        );


        return school;
    }

    @Override
    public List<SchoolListResponse> getAllSchools() {
        return schoolRepository.findAllSchoolsForDropdown();
    }

    @Override
    public void verifySchoolOtp(String email, String otp) {

        SchoolEmailOtp emailOtp = schoolEmailOtpRepository
                .findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (Boolean.TRUE.equals(emailOtp.getVerified())) {
            throw new RuntimeException("OTP already verified");
        }

        if (!emailOtp.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        LocalDateTime expiryTime =
                LocalDateTime.parse(emailOtp.getExpiryTime(), formatter);

        LocalDateTime nowIST =
                LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

        if (nowIST.isAfter(expiryTime)) {
            throw new RuntimeException("OTP expired");
        }

        emailOtp.setVerified(Boolean.TRUE);
        schoolEmailOtpRepository.save(emailOtp);

        School school = schoolRepository
                .findBySchoolEmail(email)
                .orElseThrow(() -> new RuntimeException("School not found"));

        school.setEmailVerified(Boolean.TRUE);
        schoolRepository.save(school);
    }


    @Override
    public void validateSubscription(School school) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        LocalDateTime trialEnd =
                LocalDateTime.parse(school.getTrialEndDate(), formatter);

        LocalDateTime now =
                LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

        if (now.isAfter(trialEnd)) {

            if (!SchoolConstants.PAID
                    .equals(school.getSubscriptionStatus())) {

                school.setSubscriptionStatus(
                        SchoolConstants.SUBSCRIPTION_EXPIRED);
                school.setIsActive(false);
                schoolRepository.save(school);

                throw new RuntimeException(
                        "Free trial expired. Please subscribe."
                );
            }
        }
    }
    @Override
    public School getLoggedInSchool() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("Unauthorized");
        }

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Long schoolId = userDetails.getSchoolId();

        if (schoolId == null) {
            throw new RuntimeException("User is not linked to any school");
        }

        return schoolRepository
                .findById(schoolId)
                .orElseThrow(() -> new RuntimeException("School not found"));
    }



    @Override
    @Transactional
    public void activatePaidSubscription(
            School school,
            SubscriptionRequest request) {

        Optional<SchoolSubscription> activeSub =
                schoolSubscriptionRepository
                        .findTopBySchoolIdAndPaymentStatusOrderByIdDesc(
                                school.getSchoolId(),
                                SchoolConstants.ACTIVE
                        );

        if (activeSub.isPresent()) {
            throw new RuntimeException("Active subscription already exists");
        }

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (request.getPaymentRef() == null || request.getPaymentRef().isBlank()) {
            throw new RuntimeException("Payment reference required");
        }

        SchoolSubscription subscription =
                SchoolSubscription.builder()
                        .schoolId(school.getSchoolId())
                        .planType(request.getPlanType())
                        .amount(request.getAmount())
                        .paymentRef(request.getPaymentRef())
                        .paymentStatus(SchoolConstants.ACTIVE)
                        .startDate(SchoolUtil.nowIST())
                        .endDate(
                                request.getPlanType().equals("MONTHLY")
                                        ? SchoolUtil.plusDaysIST(30)
                                        : SchoolUtil.plusDaysIST(365)
                        )
                        .build();

        schoolSubscriptionRepository.save(subscription);

        school.setSubscriptionStatus(SchoolConstants.PAID);
        school.setIsActive(true);
        schoolRepository.save(school);
    }

    @Override
    public void resendSchoolOtp(String email) {

        // 🔹 Validate email
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("School email is required");
        }

        // 🔹 Check school exists
        School school = schoolRepository
                .findBySchoolEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("School not found with this email")
                );

        // 🔹 Generate new OTP
        Integer newOtp = MailHelper.getSixDigitRandomNumber();

        // 🔹 Fetch existing OTP record (if any)
        Optional<SchoolEmailOtp> existingOtpOpt =
                schoolEmailOtpRepository.findByEmail(email);

        SchoolEmailOtp otpEntity;

        if (existingOtpOpt.isPresent()) {
            otpEntity = existingOtpOpt.get();
            otpEntity.setOtp(String.valueOf(newOtp));
            otpEntity.setVerified(Boolean.FALSE);
            otpEntity.setExpiryTime(
                    SchoolUtil.getExpiryTimeInISTString(5)
            );
        } else {
            otpEntity = new SchoolEmailOtp();
            otpEntity.setEmail(email);
            otpEntity.setOtp(String.valueOf(newOtp));
            otpEntity.setVerified(Boolean.FALSE);
            otpEntity.setExpiryTime(
                    SchoolUtil.getExpiryTimeInISTString(5)
            );
        }

        schoolEmailOtpRepository.save(otpEntity);

        // 🔹 Send mail
        mailHelper.sendOtpMail(
                email,
                school.getSchoolName(),
                String.valueOf(newOtp),
                "School OTP Resend Verification"
        );
    }


}
