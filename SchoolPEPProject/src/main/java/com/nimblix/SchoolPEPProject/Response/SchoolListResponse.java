package com.nimblix.SchoolPEPProject.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SchoolListResponse {
    private Long schoolId;
    private String schoolName;
}
