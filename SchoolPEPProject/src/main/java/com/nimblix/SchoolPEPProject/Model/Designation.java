package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Enum.StaffType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "designations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "designation_name", nullable = false)
    private String designationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_type", nullable = false)
    private StaffType staffType;
}
