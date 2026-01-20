package com.nimblix.SchoolPEPProject.Request;

import com.nimblix.SchoolPEPProject.Model.Designation;
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
    private Designation designation;

}
