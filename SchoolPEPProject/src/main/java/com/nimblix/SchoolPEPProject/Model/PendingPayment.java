package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pending_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "school_id", nullable = false)
    private Long schoolId;

    @Column(name = "plan_type", nullable = false)
    private String planType;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "razorpay_order_id", unique = true, nullable = false)
    private String razorpayOrderId;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updted_time")
    private String updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
    }
}
