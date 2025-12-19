package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Exception.UserNotFoundException;
import com.nimblix.SchoolPEPProject.Model.Classroom;
import com.nimblix.SchoolPEPProject.Model.Role;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.ClassroomRepository;
import com.nimblix.SchoolPEPProject.Repository.RoleRepository;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
import com.nimblix.SchoolPEPProject.Repository.UserRepository;
import com.nimblix.SchoolPEPProject.Request.ClassroomRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRegistrationRequest;
import com.nimblix.SchoolPEPProject.Response.TeacherDetailsResponse;
import com.nimblix.SchoolPEPProject.Service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClassroomRepository classroomRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> registerTeacher(TeacherRegistrationRequest request) {

        Map<String, String> response = new HashMap<>();

        if (request.getFirstName() == null || request.getFirstName().isBlank()
                || request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {

            response.put(SchoolConstants.MESSAGE,
                    "Missing required fields (firstName, email, password)");
            return response;
        }

        if (teacherRepository.existsByEmailId(request.getEmail())) {
            response.put(SchoolConstants.MESSAGE, "Teacher already exists with this email");
            return response;
        }

        Role teacherRole = roleRepository.findByRoleName(SchoolConstants.TEACHER_ROLE);

        // ✅ Create ONLY Teacher
        Teacher teacher = new Teacher();
        teacher.setPrefix(request.getPrefix());
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmailId(request.getEmail());
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));
        teacher.setSchoolId(1L); // TODO: from logged-in admin

        // inherited from User
        teacher.setRole(teacherRole);
        teacher.setDesignation(SchoolConstants.TEACHER_ROLE);
        teacher.setStatus(SchoolConstants.ACTIVE);
        teacher.setIsLogin(false);

        teacherRepository.save(teacher);

        response.put(SchoolConstants.MESSAGE, "Teacher Registered Successfully!");
        return response;
    }

//    @Override
//    public ResponseEntity<Teacher> getTeacherDetails(Long teacherId) {
//
//        if (teacherId == null) {
//            throw new IllegalArgumentException("Teacher ID must not be null");
//        }
//
//        return teacherRepository.findById(teacherId)
//                .orElseThrow(() ->
//                        new UserNotFoundException("Teacher not found with id: " + teacherId));
//    }



    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    public ResponseEntity<Map<String, String>> createClassroom(ClassroomRequest request) {

        Map<String, String> response = new HashMap<>();

        List<Classroom> existing = classroomRepository
                .findByClassroomNameAndSchoolId(request.getClassroomName(), request.getSchoolId());

        if (!existing.isEmpty()) {
            response.put("status", "FAIL");
            response.put("message", "Classroom already exists for this school");
            return ResponseEntity.status(409).body(response); // 🔥409 Conflict
        }

        Classroom classroom = new Classroom();
        classroom.setClassroomName(request.getClassroomName());
        classroom.setSchoolId(request.getSchoolId());
        classroom.setTeacherId(request.getTeacherId());
        classroom.setSubject(request.getSubject());
        classroomRepository.save(classroom);

        response.put("status", "SUCCESS");
        response.put("message", "Classroom created successfully");
        return ResponseEntity.ok(response); // 200
    }

    @Override
    public TeacherDetailsResponse getTeacherDetails(Long teacherId) {

        if (teacherId == null || teacherId <= 0) {
            throw new IllegalArgumentException("Teacher ID must be a positive number");
        }

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Teacher not found with id: " + teacherId
                        ));

        return TeacherDetailsResponse.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .emailId(teacher.getEmailId())
                .mobile(teacher.getMobile())
                .prefix(teacher.getPrefix())
                .designation(teacher.getDesignation())
                .gender(teacher.getGender())
                .status(teacher.getStatus())
                .build();
    }


}
