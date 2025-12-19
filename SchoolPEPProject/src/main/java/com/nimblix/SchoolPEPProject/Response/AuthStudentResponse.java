package com.nimblix.SchoolPEPProject.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthStudentResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String token;
}
