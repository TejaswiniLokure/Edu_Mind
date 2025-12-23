package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAssignmentRequest {
    private Long assignmentId;
    private String assignmentName;
    private String description;
    private Long subjectId;
    private Long schoolId;
    private Long createdByUserId;
    private Long classId;
    private String dueDate;
}
