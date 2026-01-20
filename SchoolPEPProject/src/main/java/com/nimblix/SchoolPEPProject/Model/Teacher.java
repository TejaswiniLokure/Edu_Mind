package com.nimblix.SchoolPEPProject.Model;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
public class Teacher extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_id", unique = true)
    private String teacherId;

    @Column(name = "prefix")
    private String prefix;

    @OneToMany(mappedBy = "teacher")
    private List<Subjects> subjects = new ArrayList<>();
}
