package com.nimblix.SchoolPEPProject.Response;

import com.nimblix.SchoolPEPProject.Model.Designation;
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
    private Designation designation;
    private String profilePicture;
    private Long schoolId;
}
