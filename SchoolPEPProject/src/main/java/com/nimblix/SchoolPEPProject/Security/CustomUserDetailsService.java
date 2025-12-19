package com.nimblix.SchoolPEPProject.Security;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.User;
import com.nimblix.SchoolPEPProject.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmailId(username.toLowerCase())
                .filter(u -> SchoolConstants.ACTIVE.equalsIgnoreCase(u.getStatus()))
                .orElseThrow(() ->
                        new UsernameNotFoundException("Active user not found")
                );

        return new org.springframework.security.core.userdetails.User(
                user.getEmailId(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName())
                )
        );
    }
}
