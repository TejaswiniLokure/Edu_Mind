package com.nimblix.SchoolPEPProject.Model;


import com.nimblix.SchoolPEPProject.Enum.ParentRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parents")
@DiscriminatorValue("PARENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parent extends User {


    @Column(name = "parent_id")
    private String parentId;

    @Enumerated(EnumType.STRING)
    private ParentRole parentRole;

    @ManyToMany
    @JoinTable(
            name = "parent_student_map",
            joinColumns = @JoinColumn(name = "parent_user_id"),
            inverseJoinColumns = @JoinColumn(name = "student_user_id")
    )
    private List<Student> students = new ArrayList<>();
    @Column(name = "address")
    private String address;
}

