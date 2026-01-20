package com.nimblix.SchoolPEPProject.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", unique = true)
    private String studentId;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "section")
    private String section;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "roll_no")
    private Long rollNo;

    @Column(name = "admission_no")
    private Long admissionNo;

    @Column(name = "registration_no")
    private Long registrationNo;

    @ManyToMany(mappedBy = "students")
    private List<Parent> parents = new ArrayList<>();
}
