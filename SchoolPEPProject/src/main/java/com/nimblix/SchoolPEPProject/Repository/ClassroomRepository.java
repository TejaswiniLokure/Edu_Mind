package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom,Long> {
    List<Classroom> findByClassroomNameAndSchoolId(String classroomName, Long schoolId);

    boolean existsByIdAndSchoolId(Long id, Long schoolId);

    Optional<Object> findByIdAndSchoolId(Long classRoomId, Long schoolId);
}
