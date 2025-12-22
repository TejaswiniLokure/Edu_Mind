package com.nimblix.SchoolPEPProject.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeacherRegistrationRequest {
    private String prefix;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private  Long schoolId;

}
