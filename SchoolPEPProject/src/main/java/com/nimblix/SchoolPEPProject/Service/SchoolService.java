package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.School;
import com.nimblix.SchoolPEPProject.Request.SchoolRegistrationRequest;
import com.nimblix.SchoolPEPProject.Request.SubscriptionRequest;
import com.nimblix.SchoolPEPProject.Response.SchoolListResponse;

import java.util.List;

public interface SchoolService {

    School registerSchool(SchoolRegistrationRequest request);

    List<SchoolListResponse> getAllSchools();

    void verifySchoolOtp(String email, String otp);
    
    void validateSubscription(School school);

    School getLoggedInSchool();

    void activatePaidSubscription(School school, SubscriptionRequest request);

    void resendSchoolOtp(String email);
}
