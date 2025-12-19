package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.School;
import com.nimblix.SchoolPEPProject.Response.SchoolListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School,Long> {

    boolean existsBySchoolEmail(String schoolEmail);

    @Query("""
    SELECT new com.nimblix.SchoolPEPProject.Response.SchoolListResponse(
        s.id,
        s.schoolName
    )
    FROM School s
""")
    List<SchoolListResponse> findAllSchoolsForDropdown();


    Optional<School> findBySchoolEmail(String schoolEmail);
}
