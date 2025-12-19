package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.School;
import com.nimblix.SchoolPEPProject.Request.OtpVerifyRequest;
import com.nimblix.SchoolPEPProject.Request.SchoolRegistrationRequest;
import com.nimblix.SchoolPEPProject.Request.SubscriptionRequest;
import com.nimblix.SchoolPEPProject.Response.SchoolListResponse;
import com.nimblix.SchoolPEPProject.Service.SchoolService;
import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/school")
@RestController
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerSchool(
            @RequestBody SchoolRegistrationRequest request) {

        School school = schoolService.registerSchool(request);

        Map<String, Object> response = new HashMap<>();
        response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
        response.put(SchoolConstants.MESSAGE, "School registered successfully");
        response.put("data", school.getSchoolId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/resendOtp")
    public ResponseEntity<Map<String, Object>> resendOtp(
            @RequestParam String email) {


        schoolService.resendSchoolOtp(email);

        return ResponseEntity.ok(
                Map.of(
                        SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS,
                        SchoolConstants.MESSAGE, "OTP resent successfully"
                )
        );
    }


    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listSchools() {
        List<SchoolListResponse> schools = schoolService.getAllSchools();
        Map<String, Object> response = new HashMap<>();
        response.put(SchoolConstants.SCHOOLS_LIST, schools);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/verifyOtp")
    public ResponseEntity<Map<String, Object>> verifyOtp(
            @RequestBody OtpVerifyRequest request) {

        schoolService.verifySchoolOtp(
                request.getEmail(),
                request.getOtp()
        );

        return ResponseEntity.ok(
                Map.of(
                        SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS,
                        SchoolConstants.MESSAGE, "OTP verified successfully"
                )
        );
    }


    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(
            @RequestBody SubscriptionRequest request) {

        School school = schoolService.getLoggedInSchool();

        schoolService.activatePaidSubscription(school, request);

        return ResponseEntity.ok(
                Map.of(
                        SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS,
                        SchoolConstants.MESSAGE,
                        SchoolConstants.SUBSCRIPTION_ACTIVATED_SUCCESSFULLY,
                        SchoolConstants.SUBSCRIPTION_STATUS,
                        SchoolConstants.PAID
                )
        );
    }


    @GetMapping("/subscription")
    public ResponseEntity<Map<String, Object>> getSubscriptionStatus() {

        School school = schoolService.getLoggedInSchool();

        schoolService.validateSubscription(school);

        Map<String, Object> response = new HashMap<>();
        response.put(
                SchoolConstants.SUBSCRIPTION_STATUS,
                school.getSubscriptionStatus()
        );
        response.put(
                SchoolConstants.TRAIL_START_DATE,
                school.getTrialStartDate()
        );
        response.put(
                SchoolConstants.TRAIL_END_DATE,
                school.getTrialEndDate()
        );
        response.put(
                SchoolConstants.DAYS_REMAINING,
                SchoolUtil.daysRemaining(school.getTrialEndDate())
        );

        return ResponseEntity.ok(response);
    }




}
