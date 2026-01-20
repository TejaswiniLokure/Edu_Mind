package com.nimblix.SchoolPEPProject.Model;


import com.nimblix.SchoolPEPProject.Enum.ParentRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "parents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parent extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id", unique = true)
    private String parentId;

    @Enumerated(EnumType.STRING)
    private ParentRole parentRole;

    @ManyToMany
    @JoinTable(
            name = "parent_student_map",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students = new ArrayList<>();
}
