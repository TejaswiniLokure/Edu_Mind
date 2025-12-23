package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassroomRequest {
    private String classroomName;
    private Long schoolId;
    private String subject;
    private String teacherId;
}
