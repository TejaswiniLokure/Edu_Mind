package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentDetailsResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobile;
    private Long schoolId;
    private String status;
    private Long classId;
    private String section;
    private String roleName;
}
