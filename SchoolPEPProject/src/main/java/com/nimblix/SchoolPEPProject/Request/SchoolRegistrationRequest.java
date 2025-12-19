package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolRegistrationRequest {

    private Long schoolId;
    private String schoolName;
    private String schoolAddress;
    private String schoolPhone;
    private String schoolEmail;
    private Double latitude;
    private Double longitude;
    private String password;

    // GPS / MANUAL
    private String locationType;
}
