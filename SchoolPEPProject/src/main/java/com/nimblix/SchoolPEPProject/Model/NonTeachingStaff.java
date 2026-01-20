package com.nimblix.SchoolPEPProject.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "non_teaching_staff")
@Getter
@Setter
@NoArgsConstructor
public class NonTeachingStaff extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "staff_code", unique = true)
    private String staffCode;
}
