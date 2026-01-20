package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    @Query("""
        SELECT s FROM Student s
        WHERE s.schoolId = :schoolId
          AND (:classId IS NULL OR s.classId = :classId)
          AND (:section IS NULL OR s.section = :section)
          AND (:status IS NULL OR s.status = :status)
    """)
    List<Student> findByAllFilters(
            @Param("schoolId") Long schoolId,
            @Param("classId") Long classId,
            @Param("section") String section,
            @Param("status") String status
    );

    boolean existsByEmailId(String email);

    List<Student> findBySchoolId(Long schoolId);

    Student findByStudentId(String studentId);

    Optional<Student> findByEmailId(String email);
}
