package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent,Long> {

    boolean existsByEmailId(String emailId);

    Optional<Parent> findByEmailId(String email);
}
