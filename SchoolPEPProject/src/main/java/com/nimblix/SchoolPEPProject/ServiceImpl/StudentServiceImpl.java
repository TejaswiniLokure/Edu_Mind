package com.nimblix.SchoolPEPProject.ServiceImpl;
import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Exception.UserNotFoundException;
import com.nimblix.SchoolPEPProject.Model.Role;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Model.User;
import com.nimblix.SchoolPEPProject.Repository.RoleRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
import com.nimblix.SchoolPEPProject.Repository.UserRepository;
import com.nimblix.SchoolPEPProject.Request.StudentRegistrationRequest;
import com.nimblix.SchoolPEPProject.Response.StudentDetailsResponse;
import com.nimblix.SchoolPEPProject.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<?> registerStudent(StudentRegistrationRequest request) {

        if (!request.getPassword().equals(request.getReEnterPassword())) {
            return ResponseEntity.badRequest()
                    .body("Password and Re-Enter Password do not match!");
        }

        if (studentRepository.existsByEmailId(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Email already registered!");
        }

        // 3️⃣ Fetch role
        Role studentRole = roleRepository.findByRoleName(SchoolConstants.STUDENT);

        // 4️⃣ Create ONLY Student
        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmailId(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setSchoolId(request.getSchoolId());
        student.setStatus(SchoolConstants.ACTIVE);
        student.setIsLogin(Boolean.FALSE);
        student.setDesignation(SchoolConstants.STUDENT);
        student.setRole(studentRole);

        Student savedStudent = studentRepository.save(student);

        return ResponseEntity.ok("Student registered successfully with ID: " + savedStudent.getId());
    }

    @Override
    public void updateStudentDetails(Long studentId, StudentRegistrationRequest request) {

        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        if(request.getFirstName()!=null && request.getLastName()!=null){
            existingStudent.setFirstName(request.getFirstName());
            existingStudent.setLastName(request.getLastName());
        }

        if (request.getEmail() != null) {
            existingStudent.setEmailId(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {

            if (!request.getPassword().equals(request.getReEnterPassword())) {
                throw new RuntimeException("Password and Re-Enter Password do not match!");
            }

            existingStudent.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
        }

        if (request.getSchoolId() != null) {
            existingStudent.setSchoolId(request.getSchoolId());
        }

        studentRepository.save(existingStudent);
    }

    @Override
    public List<StudentDetailsResponse> getStudentsBySchoolId(Long schoolId) {

        if (schoolId == null || schoolId <= 0) {
            throw new IllegalArgumentException("School ID must be valid");
        }

        List<Student> students = studentRepository.findBySchoolId(schoolId);

        if (students.isEmpty()) {
            throw new UserNotFoundException(
                    "No students found for schoolId: " + schoolId
            );
        }

        return students.stream()
                .map(student -> StudentDetailsResponse.builder()
                        .id(student.getId())
                        .firstName(student.getFirstName())
                        .lastName(student.getLastName())
                        .emailId(student.getEmailId())
                        .mobile(student.getMobile())
                        .status(student.getStatus())
                        .classId(student.getClassId())
                        .section(student.getSection())
                        .build()
                )
                .toList();
    }


    @Override
    public void deleteStudent(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        // Soft delete
        student.setStatus(SchoolConstants.IN_ACTIVE);
        studentRepository.save(student);
    }
}
