package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AdminAccountCreateRequest {
    private String adminMobileNo;
    private String adminFirstName;
    private String adminLastName;
    private String email;
    private String password;
    private String reEnterPassword;
    private String designation;

}
