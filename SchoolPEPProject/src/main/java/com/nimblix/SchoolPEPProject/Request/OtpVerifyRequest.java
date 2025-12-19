package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerifyRequest {
    private String email;
    private String otp;
}
