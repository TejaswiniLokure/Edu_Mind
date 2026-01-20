package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.*;
import com.nimblix.SchoolPEPProject.Repository.*;
import com.nimblix.SchoolPEPProject.Request.AuthLoginRequest;
import com.nimblix.SchoolPEPProject.Response.AuthLoginResponse;
import com.nimblix.SchoolPEPProject.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginRequest request) {

        try {

            if (request.getEmail() == null || request.getEmail().isBlank())
                return ResponseEntity.badRequest()
                        .body(Map.of(SchoolConstants.MESSAGE, "Email is required"));

            if (request.getRole() == null || request.getRole().isBlank())
                return ResponseEntity.badRequest()
                        .body(Map.of(SchoolConstants.MESSAGE, "Role is required"));

            String requestRole = request.getRole().toUpperCase();
            User user = null;

            switch (requestRole) {
                case SchoolConstants.ADMIN ->
                        user = adminRepository.findByEmailId(request.getEmail()).orElse(null);
                case SchoolConstants.TEACHER ->
                        user = teacherRepository.findByEmailId(request.getEmail()).orElse(null);
                case SchoolConstants.STUDENT ->
                        user = studentRepository.findByEmailId(request.getEmail()).orElse(null);
                case SchoolConstants.PARENT ->
                        user = parentRepository.findByEmailId(request.getEmail()).orElse(null);
            }

            if (user == null || !SchoolConstants.ACTIVE.equalsIgnoreCase(user.getStatus()))
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(SchoolConstants.MESSAGE, "User not found or inactive"));

            String dbRole = user.getRole().getRoleName().toUpperCase();
            if (!dbRole.equals(requestRole))
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(SchoolConstants.MESSAGE, SchoolConstants.ROLE_MISMATCH));

            if (SchoolConstants.STUDENT.equals(dbRole)) {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
            }

            String token = jwtUtil.generateToken(
                    userDetailsService.loadUserByUsername(request.getEmail())
            );

            user.setIsLogin(true);

            if (user instanceof Admin admin) adminRepository.save(admin);
            else if (user instanceof Teacher teacher) teacherRepository.save(teacher);
            else if (user instanceof Student student) studentRepository.save(student);
            else if (user instanceof Parent parent) parentRepository.save(parent);

            AuthLoginResponse response = new AuthLoginResponse();
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setEmail(user.getEmailId());
            response.setRole(dbRole);
            response.setToken(token);

            if (user instanceof Admin admin) response.setUserId(admin.getId());
            else if (user instanceof Teacher teacher) response.setUserId(teacher.getId());
            else if (user instanceof Student student) response.setUserId(student.getId());
            else if (user instanceof Parent parent) response.setUserId(parent.getId());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(SchoolConstants.MESSAGE, "Invalid password"));
        }
    }


}
