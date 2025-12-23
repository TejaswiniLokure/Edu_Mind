package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
public class Subjects {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "subject_code")
    private String code;

    @Column(name = "sub_desription")
    private String subDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;


    @Column(name = "class_room_id")
    private Long classRoomId;

    @Column(name = "total_marks")
    private  Long totalMarks;

    @Column(name = "marks_obtained")
    private Long marksObtained;

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