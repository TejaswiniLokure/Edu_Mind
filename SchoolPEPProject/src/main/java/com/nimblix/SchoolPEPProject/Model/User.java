package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Enum.StaffType;
import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.*;
@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email_id", unique = true, nullable = false)
    private String emailId;

    @Column(name = "password")
    private String password;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "school_id")
    private Long schoolId;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "is_login")
    private Boolean isLogin;

    /** MAIN CATEGORY */
    @Enumerated(EnumType.STRING)
    @Column(name = "staff_type")
    private StaffType staffType;


    @ManyToOne
    @JoinColumn(name = "designation_id")
    private Designation designation;

    @Column(name = "gender")
    private String gender;

    @Column(name = "status")
    private String status;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;

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
