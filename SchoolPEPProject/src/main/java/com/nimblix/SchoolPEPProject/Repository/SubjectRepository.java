package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subjects,Long> {
    boolean existsByIdAndClassRoomId(Long id, Long classRoomId);

    Optional<Object> findBySubjectNameAndClassRoomIdAndTeacher_Id(String subjectName, Long classRoomId, Long teacherId);
}
