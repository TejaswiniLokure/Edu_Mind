package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.PendingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingPaymentRepository extends JpaRepository<PendingPayment, Long> {

    Optional<PendingPayment> findByStripePaymentIntentId(String stripePaymentIntentId);
}
