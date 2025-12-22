package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "home_work")
@Getter
@Setter
@RequiredArgsConstructor
public class HomeWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "school_id")
    private Long schoolId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "description")
    private  String description;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;


    @PrePersist
    protected void onCreate(){
        createdTime= SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
        updatedTime= SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();

    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedTime= SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();


    }
}
