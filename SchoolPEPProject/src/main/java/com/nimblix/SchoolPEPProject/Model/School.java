package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "school")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class School {

    @Id
    @Column(name = "school_id")
    private Long schoolId;

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @Column(name = "school_address")
    private String schoolAddress;

    @Column(name = "school_phone")
    private String schoolPhone;

    @Column(name = "school_email")
    private String schoolEmail;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "location_type")
    private String locationType;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "subscription_status")
    private String subscriptionStatus;

    @Column(name = "trial_start_date")
    private String trialStartDate;

    @Column(name = "trial_end_date")
    private String trialEndDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @PrePersist
    protected void onCreate() {
        createdTime = SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
        updatedTime = createdTime;

        subscriptionStatus = SchoolConstants.SUBSCRIPTION_TRAIL;
        trialStartDate = SchoolUtil.nowIST();
        trialEndDate = SchoolUtil.plusDaysIST(30);
        isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
    }
}

