package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeacherDetailsResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobile;
    private String prefix;
    private String designation;
    private String gender;
    private String status;
}
