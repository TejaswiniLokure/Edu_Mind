package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.User;
import com.nimblix.SchoolPEPProject.Repository.UserRepository;
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
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginRequest request) {

        try {
            // 1️⃣ Validate email
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of(SchoolConstants.MESSAGE, "Email is required"));
            }

            // 2️⃣ Validate role
            if (request.getRole() == null || request.getRole().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of(SchoolConstants.MESSAGE, "Role is required"));
            }

            // 3️⃣ Fetch user
            User user = userRepository
                    .findByEmailId(request.getEmail())
                    .filter(u -> SchoolConstants.ACTIVE.equalsIgnoreCase(u.getStatus()))
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User not found or inactive"));
            }

            String dbRole = user.getRole().getRoleName().toUpperCase();
            String requestRole = request.getRole().toUpperCase();

            // 4️⃣ Role validation
            if (!dbRole.equals(requestRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Role mismatch"));
            }

            // 5️⃣ Password check ONLY for STUDENT
            if (SchoolConstants.STUDENT.equals(dbRole)) {

                if (request.getPassword() == null || request.getPassword().isBlank()) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("message", "Password is required for student login"));
                }

                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
            }

            // 6️⃣ Generate JWT
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(request.getEmail());

            String token = jwtUtil.generateToken(userDetails);

            // 7️⃣ Update login flag
            user.setIsLogin(true);
            userRepository.save(user);

            // 8️⃣ Build response
            AuthLoginResponse response = new AuthLoginResponse();
            response.setUserId(user.getId());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setEmail(user.getEmailId());
            response.setRole(dbRole);
            response.setToken(token);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid password"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Login failed"));
        }
    }

}
