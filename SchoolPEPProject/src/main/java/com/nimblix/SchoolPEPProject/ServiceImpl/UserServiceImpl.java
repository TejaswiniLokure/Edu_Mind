package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Enum.StaffType;
import com.nimblix.SchoolPEPProject.Model.Designation;
import com.nimblix.SchoolPEPProject.Model.Parent;
import com.nimblix.SchoolPEPProject.Model.Role;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Repository.DesignationRepository;
import com.nimblix.SchoolPEPProject.Repository.ParentRepository;
import com.nimblix.SchoolPEPProject.Repository.RoleRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
import com.nimblix.SchoolPEPProject.Request.UserRegistrationRequest;
import com.nimblix.SchoolPEPProject.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final DesignationRepository designationRepository;

    @Override
    public ResponseEntity<?> registerUser(UserRegistrationRequest request) {

        if (!request.getPassword().equals(request.getReEnterPassword())) {
            return ResponseEntity.badRequest()
                    .body("Password and Re-Enter Password do not match");
        }

        if (studentRepository.existsByEmailId(request.getEmail()) ||
                parentRepository.existsByEmailId(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Email already registered");
        }

        if (SchoolConstants.STUDENT.equalsIgnoreCase(request.getRole())) {

            if (request.getStudentId() == null || request.getStudentId().isBlank()) {
                return ResponseEntity.badRequest().body("Student ID is required");
            }

            Role studentRole =
                    roleRepository.findByRoleName(SchoolConstants.STUDENT);

            Designation studentDesignation =
                    designationRepository.findByDesignationName("Student")
                            .orElseThrow(() -> new RuntimeException("Student designation not found"));

            Student student = new Student();
            student.setStudentId(request.getStudentId());
            student.setFirstName(request.getFirstName());
            student.setLastName(request.getLastName());
            student.setEmailId(request.getEmail());
            student.setPassword(passwordEncoder.encode(request.getPassword()));
            student.setMobile(request.getMobileNumber());
            student.setSchoolId(request.getSchoolId());
            student.setStatus(SchoolConstants.ACTIVE);
            student.setIsLogin(Boolean.FALSE);

            student.setStaffType(StaffType.NON_TEACHING);
            student.setDesignation(studentDesignation);
            student.setRole(studentRole);

            studentRepository.save(student);

            return ResponseEntity.ok("Student registered successfully!");

        } else if (SchoolConstants.PARENT.equalsIgnoreCase(request.getRole())) {

            if (request.getParentId() == null || request.getParentId().isBlank()) {
                return ResponseEntity.badRequest().body("Parent ID is required");
            }

            Role parentRole =
                    roleRepository.findByRoleName(SchoolConstants.PARENT);

            Student student =
                    studentRepository.findByStudentId(request.getStudentId());

            Designation parentDesignation =
                    designationRepository.findByDesignationName("Parent")
                            .orElseThrow(() -> new RuntimeException("Parent designation not found"));

            Parent parent = new Parent();
            parent.setParentId(request.getParentId());
            parent.setFirstName(request.getFirstName());
            parent.setLastName(request.getLastName());
            parent.setEmailId(request.getEmail());
            parent.setPassword(passwordEncoder.encode(request.getPassword()));
            parent.setMobile(request.getMobileNumber());
            parent.setStudents(List.of(student));
            parent.setSchoolId(student.getSchoolId());
            parent.setStatus(SchoolConstants.ACTIVE);

            parent.setStaffType(StaffType.NON_TEACHING);
            parent.setDesignation(parentDesignation);
            parent.setRole(parentRole);

            parentRepository.save(parent);

            return ResponseEntity.ok("Parent registered successfully!");
        }

        return ResponseEntity.badRequest().body("Invalid role");
    }

}
