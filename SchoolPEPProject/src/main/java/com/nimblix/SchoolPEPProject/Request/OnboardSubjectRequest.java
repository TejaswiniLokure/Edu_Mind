package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnboardSubjectRequest {
    private Long schoolId;
    private Long classRoomId;
    private Long teacherId;

    private String subjectName;
    private String subjectCode;
    private String subjectDescription;

    private Long totalMarks;
}
