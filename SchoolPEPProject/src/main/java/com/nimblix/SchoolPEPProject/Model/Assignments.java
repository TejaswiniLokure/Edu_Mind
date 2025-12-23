package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
public class Assignments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assignment_name")
    private String assignmentName;

    @Column(name = "description")
    private String description;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "school_id")
    private Long schoolId;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "created_time", updatable = false)
    private String createdTime;

    @Column(name = "assigned_to_user_id")
    private Long assignedToUserId;

    @Column(name = "updated_time")
    private String updatedTime;

    @OneToMany(
            mappedBy = "assignment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Attachments> attachments = new ArrayList<>();

    @OneToMany(
            mappedBy = "assignment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();

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
