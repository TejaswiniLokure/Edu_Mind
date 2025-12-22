package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    boolean existsByEmailId(String email);

    @Query("SELECT t FROM Teacher t WHERE t.id = :teacherId AND t.schoolId = :schoolId")
    Teacher findByTeacherIdAndSchoolId(@Param("teacherId") Long teacherId,
                                       @Param("schoolId") Long schoolId);
}
