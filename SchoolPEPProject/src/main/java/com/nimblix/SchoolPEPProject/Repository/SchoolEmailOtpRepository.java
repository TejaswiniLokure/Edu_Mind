package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.SchoolEmailOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolEmailOtpRepository extends JpaRepository<SchoolEmailOtp,Long> {

    Optional<SchoolEmailOtp> findTopByEmailOrderByIdDesc(String email);

    Optional<SchoolEmailOtp> findByEmail(String email);
}
