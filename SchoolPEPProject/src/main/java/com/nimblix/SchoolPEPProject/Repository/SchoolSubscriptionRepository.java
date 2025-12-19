package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.SchoolSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolSubscriptionRepository extends JpaRepository<SchoolSubscription,Long> {

    Optional<SchoolSubscription>
    findTopBySchoolIdAndPaymentStatusOrderByIdDesc(
            Long schoolId,
            String paymentStatus
    );}
