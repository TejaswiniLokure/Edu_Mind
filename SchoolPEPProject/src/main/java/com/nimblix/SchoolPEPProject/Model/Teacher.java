package com.nimblix.SchoolPEPProject.Model;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@DiscriminatorValue("TEACHER")
@Getter
@Setter
@NoArgsConstructor
public class Teacher extends User {


    @Column(name = "teacher_id")
    private String teacherId;

    @Column(name = "prefix")
    private String prefix;

    @OneToMany(mappedBy = "teacher")
    private List<Subjects> subjects = new ArrayList<>();
}