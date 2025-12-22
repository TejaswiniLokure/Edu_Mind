package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@RequiredArgsConstructor
public class Assignments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assignment_name")
    private String assignmentName;

    @Column(name = "description")
    private String description;

    @Column(name = "school_id")
    private Long schoolId;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @Column(name = "assigned_to_user_id")
    private Long assignedToUserId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
        updatedTime = createdTime;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
    }
}
