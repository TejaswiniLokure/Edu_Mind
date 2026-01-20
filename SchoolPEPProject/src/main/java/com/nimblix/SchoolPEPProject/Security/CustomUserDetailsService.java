package com.nimblix.SchoolPEPProject.Security;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.User;
import com.nimblix.SchoolPEPProject.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        String email = username.toLowerCase();

        User user =
                adminRepository.findByEmailId(email)
                        .filter(u -> SchoolConstants.ACTIVE.equalsIgnoreCase(u.getStatus()))
                        .map(u -> (User) u)
                        .or(() -> teacherRepository.findByEmailId(email)
                                .filter(u -> SchoolConstants.ACTIVE.equalsIgnoreCase(u.getStatus()))
                                .map(u -> (User) u))
                        .or(() -> studentRepository.findByEmailId(email)
                                .filter(u -> SchoolConstants.ACTIVE.equalsIgnoreCase(u.getStatus()))
                                .map(u -> (User) u))
                        .or(() -> parentRepository.findByEmailId(email)
                                .filter(u -> SchoolConstants.ACTIVE.equalsIgnoreCase(u.getStatus()))
                                .map(u -> (User) u))
                        .orElseThrow(() ->
                                new UsernameNotFoundException("Active user not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmailId(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().getRoleName()
                ))
        );
    }
}
