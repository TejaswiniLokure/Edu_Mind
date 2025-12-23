package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthLoginRequest {
    private String email;
    private String password;
    private String role;

}
