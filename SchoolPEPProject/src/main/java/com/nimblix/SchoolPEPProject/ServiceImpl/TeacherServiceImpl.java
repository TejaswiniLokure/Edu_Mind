package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Exception.UserNotFoundException;
import com.nimblix.SchoolPEPProject.Helper.UploadImageHelper;
import com.nimblix.SchoolPEPProject.Model.*;
import com.nimblix.SchoolPEPProject.Repository.*;
import com.nimblix.SchoolPEPProject.Request.ClassroomRequest;
import com.nimblix.SchoolPEPProject.Request.CreateAssignmentRequest;
import com.nimblix.SchoolPEPProject.Request.OnboardSubjectRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRegistrationRequest;
import com.nimblix.SchoolPEPProject.Response.MultipleImageResponse;
import com.nimblix.SchoolPEPProject.Response.TeacherDetailsResponse;
import com.nimblix.SchoolPEPProject.Service.TeacherService;
import com.nimblix.SchoolPEPProject.Util.UserIdGeneratorUtil;
import com.nimblix.SchoolPEPProject.Util.UserType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SchoolRepository schoolRepository;
    private final SubjectRepository subjectRepository;
    private final ClassroomRepository classroomRepository;
    private final AttachmentsRepository attachmentsRepository;
    private final AssignmentsRepository assignmentsRepository;
    private final PasswordEncoder passwordEncoder;
    private final UploadImageHelper  uploadImageHelper;
    Map<String, String> response = new HashMap<>();

    @Override
    public Map<String, String> registerTeacher(TeacherRegistrationRequest request) {

        Map<String, String> response = new HashMap<>();

        if (request.getFirstName() == null || request.getFirstName().isBlank()
                || request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {

            response.put(
                    SchoolConstants.MESSAGE,
                    "Missing required fields (firstName, email, password)"
            );
            return response;
        }

        if (teacherRepository.existsByEmailId(request.getEmail())) {
            response.put(
                    SchoolConstants.MESSAGE,
                    "Teacher already exists with this email"
            );
            return response;
        }

        Role teacherRole = roleRepository.findByRoleName(SchoolConstants.TEACHER_ROLE);

        Teacher teacher = new Teacher();
        teacher.setPrefix(request.getPrefix());
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmailId(request.getEmail());
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));
        teacher.setSchoolId(1L); // TODO: get from logged-in admin

        teacher.setRole(teacherRole);
        teacher.setDesignation(SchoolConstants.TEACHER_ROLE);
        teacher.setStatus(SchoolConstants.ACTIVE);
        teacher.setIsLogin(false);

        Teacher savedTeacher = teacherRepository.save(teacher);

        String teacherId = UserIdGeneratorUtil.generateUserId(
                UserType.TEACHER,
                savedTeacher.getId()
        );

        savedTeacher.setTeacherId(teacherId);

        teacherRepository.save(savedTeacher);

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
            response.put(SchoolConstants.STATUS,SchoolConstants.STATUS_FAILURE);
            response.put(SchoolConstants.MESSAGE, "Classroom already exists for this school");
            return ResponseEntity.status(409).body(response);
        }

        Classroom classroom = new Classroom();
        classroom.setClassroomName(request.getClassroomName());
        classroom.setSchoolId(request.getSchoolId());
        classroom.setTeacherId(request.getTeacherId());
        classroom.setSubject(request.getSubject());
        classroomRepository.save(classroom);

        response.put(SchoolConstants.STATUS,SchoolConstants.STATUS_SUCCESS);
        response.put(SchoolConstants.MESSAGE, "Classroom created successfully");
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

    @Override
    public Map<String, String> updateTeacherDetails(TeacherRegistrationRequest request, Long teacherId) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);

        Map<String, String> response = new HashMap<>();

        if (teacherOptional.isEmpty()) {
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_ERORR);
            response.put(SchoolConstants.MESSAGE, "Teacher not found");
            return response;
        }

        Teacher teacher = teacherOptional.get();
        teacher.setPrefix(request.getPrefix());
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmailId(request.getEmail());
        teacher.setSchoolId(request.getSchoolId());
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));
        teacherRepository.save(teacher);

        response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
        response.put(SchoolConstants.MESSAGE, "Teacher details updated successfully");
        return response;
    }

    @Override
    public Map<String, String> deleteTeacherDetails(Long teacherId, Long schoolId) {

        Teacher teacher= teacherRepository.findByTeacherIdAndSchoolId(teacherId,schoolId);
        teacherRepository.delete(teacher);
        response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
        response.put(SchoolConstants.MESSAGE, "Teacher details deleted successfully");
        return response;
    }

    @SneakyThrows
    @Override
    public Map<String, String> createAssignment(
            CreateAssignmentRequest request,
            MultipartFile[] files
    ) {

        if (!schoolRepository.existsById(request.getSchoolId())) {
            throw new IllegalArgumentException("Invalid School ID");
        }
      log.info("School Id is "+request.getSchoolId());
        if (!classroomRepository.existsByIdAndSchoolId(
                request.getClassId(),
                request.getSchoolId())) {
            throw new IllegalArgumentException("Invalid Class ID for given School");
        }
      log.info("Classroom Id is "+request.getClassId());
        if (!subjectRepository.existsByIdAndClassRoomId(
                request.getSubjectId(),
                request.getClassId())) {
            throw new IllegalArgumentException("Invalid Subject ID for given Class");
        }

        log.info("Subject Id is "+request.getSubjectId());
        if (!userRepository.existsByIdAndRole_RoleName(
                request.getCreatedByUserId(),
                SchoolConstants.TEACHER_ROLE)) {
            throw new IllegalArgumentException("Invalid Teacher ID");
        }

        Assignments assignment = new Assignments();
        assignment.setAssignmentName(request.getAssignmentName());
        assignment.setDescription(request.getDescription());
        assignment.setSubjectId(request.getSubjectId());
        assignment.setSchoolId(request.getSchoolId());
        assignment.setClassId(request.getClassId());
        assignment.setCreatedByUserId(request.getCreatedByUserId());
        assignment.setDueDate(request.getDueDate());

        Assignments savedAssignment =
                assignmentsRepository.save(assignment);
       log.info("Saved Assignment "+savedAssignment);
        // 6️⃣ Handle Attachments
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {

                MultipleImageResponse uploadResponse =
                        uploadImageHelper.uploadImages(List.of(file));

                if (SchoolConstants.STATUS_SUCCESS.equals(uploadResponse.getStatus())
                        && uploadResponse.getUploadedFileNames() != null
                        && !uploadResponse.getUploadedFileNames().isEmpty()) {

                    Attachments attachment = new Attachments();
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setFileUrl(uploadResponse.getUploadedFileNames().get(0));
                    attachment.setAssignment(savedAssignment);

                    attachmentsRepository.save(attachment);
                }
                log.info("Uploaded File "+file.getOriginalFilename());
            }
        }

        return Map.of(
                SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS,
                SchoolConstants.MESSAGE, SchoolConstants.ASSIGNMENT_CREATED_SUCCESSFULLY
        );
    }

    public Map<String, String> onboardSubject(OnboardSubjectRequest request) {

        Classroom classroom = (Classroom) classroomRepository
                .findByIdAndSchoolId(request.getClassRoomId(), request.getSchoolId())
                .orElseThrow(() -> new RuntimeException("Invalid class or school"));

        Teacher teacher = teacherRepository
                .findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        boolean exists = subjectRepository
                .findBySubjectNameAndClassRoomIdAndTeacher_Id(
                        request.getSubjectName(),
                        request.getClassRoomId(),
                        request.getTeacherId()
                )
                .isPresent();

        if (exists) {
            return Map.of(
                    SchoolConstants.STATUS, SchoolConstants.STATUS_ERORR,
                    SchoolConstants.MESSAGE, "Subject already onboarded for this class"
            );
        }

        Subjects subject = new Subjects();
        subject.setSubjectName(request.getSubjectName());
        subject.setCode(request.getSubjectCode());
        subject.setSubDescription(request.getSubjectDescription());
        subject.setTeacher(teacher);
        subject.setClassRoomId(request.getClassRoomId());
        subject.setTotalMarks(request.getTotalMarks());

        subjectRepository.save(subject);

        return Map.of(
                SchoolConstants.STATUS,SchoolConstants.STATUS_SUCCESS ,
                SchoolConstants.MESSAGE, "Subject onboarded successfully"
        );
    }

    @SneakyThrows
    @Override
    public Map<String, String> updateAssignment(
            CreateAssignmentRequest request,
            MultipartFile[] files
    ) {

        // 1️⃣ Validate assignment
        Assignments assignment = assignmentsRepository
                .findById(request.getAssignmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

        // 2️⃣ Validate school
        if (!schoolRepository.existsById(request.getSchoolId())) {
            throw new IllegalArgumentException("Invalid School ID");
        }

        // 3️⃣ Validate class belongs to school
        if (!classroomRepository.existsByIdAndSchoolId(
                request.getClassId(),
                request.getSchoolId())) {
            throw new IllegalArgumentException("Invalid Class ID for given School");
        }

        // 4️⃣ Validate subject belongs to class
        if (!subjectRepository.existsByIdAndClassRoomId(
                request.getSubjectId(),
                request.getClassId())) {
            throw new IllegalArgumentException("Invalid Subject ID for given Class");
        }

        // 5️⃣ Validate teacher
        if (!userRepository.existsByIdAndRole_RoleName(
                request.getCreatedByUserId(),
                SchoolConstants.TEACHER_ROLE)) {
            throw new IllegalArgumentException("Invalid Teacher ID");
        }

        // 6️⃣ Update assignment fields
        assignment.setAssignmentName(request.getAssignmentName());
        assignment.setDescription(request.getDescription());
        assignment.setSubjectId(request.getSubjectId());
        assignment.setSchoolId(request.getSchoolId());
        assignment.setClassId(request.getClassId());
        assignment.setCreatedByUserId(request.getCreatedByUserId());
        assignment.setDueDate(request.getDueDate());

        Assignments updatedAssignment =
                assignmentsRepository.save(assignment);

        log.info("Updated Assignment {}", updatedAssignment);

        // 7️⃣ Handle attachments (APPEND logic)
        if (files != null && files.length > 0) {

            for (MultipartFile file : files) {

                MultipleImageResponse uploadResponse =
                        uploadImageHelper.uploadImages(List.of(file));

                if (SchoolConstants.STATUS_SUCCESS.equals(uploadResponse.getStatus())
                        && uploadResponse.getUploadedFileNames() != null
                        && !uploadResponse.getUploadedFileNames().isEmpty()) {

                    Attachments attachment = new Attachments();
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setFileUrl(uploadResponse.getUploadedFileNames().get(0));
                    attachment.setAssignment(updatedAssignment);

                    attachmentsRepository.save(attachment);
                }

                log.info("Updated Attachment {}", file.getOriginalFilename());
            }
        }

        return Map.of(
                SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS,
                SchoolConstants.MESSAGE, "Assignment updated successfully"
        );
    }


}
