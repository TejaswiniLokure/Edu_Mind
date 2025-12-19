package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "school_subscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "school_id")
    private Long schoolId;

    @Column(name = "plan_type")
    private String planType;   // MONTHLY / YEARLY

    @Column(name = "amount")
    private Double amount;

    @Column(name = "payment_ref", unique = true)
    private String paymentRef;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

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

