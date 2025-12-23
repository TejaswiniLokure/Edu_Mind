package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Locale;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE UPPER(u.emailId) = UPPER(:email)")
    Optional<User> findByEmailId(@Param("email") String email);


    boolean existsByEmailId(String email);
    Optional<User> findByEmailIdIgnoreCase(String emailId);

    boolean existsByIdAndRole_RoleName(Long createdByUserId, String teacherRole);
}
