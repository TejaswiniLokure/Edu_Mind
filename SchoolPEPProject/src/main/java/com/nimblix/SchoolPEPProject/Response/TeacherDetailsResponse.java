package com.nimblix.SchoolPEPProject.Response;

import com.nimblix.SchoolPEPProject.Model.Designation;
import com.nimblix.SchoolPEPProject.Repository.DesignationRepository;
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
    private Designation designation;
    private String gender;
    private String status;
}
