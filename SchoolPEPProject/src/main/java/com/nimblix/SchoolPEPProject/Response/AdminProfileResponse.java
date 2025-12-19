package com.nimblix.SchoolPEPProject.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProfileResponse {
    private Long adminId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobile;
    private String gender;
    private String designation;
    private String profilePicture;
    private Long schoolId;
}
