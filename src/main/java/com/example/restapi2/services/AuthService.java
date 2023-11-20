package com.example.restapi2.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restapi2.dto.LoginResponseDto;
import com.example.restapi2.models.Role;
import com.example.restapi2.models.User;
import com.example.restapi2.repositories.RoleRepository;
import com.example.restapi2.repositories.UserRepository;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    public User registerUser(String username, String password) {

        String encodedPassword = passwordEncoder.encode(password);

        Role userRole = roleRepository.findByAuthority("ROLE_USER").get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        return userRepository.save(new User(null, "firstname", "lastname", username,
                encodedPassword, authorities));
    }

    public LoginResponseDto loginUser(String username, String password) {

        try {

            System.out.println("\n\n***************" + username + "***************\n\n");
            System.out.println(
                    "\n\n***************" + userRepository.findByEmail(username).get() + "***************\n\n");
            System.out.println(
                    "\n\n***************" + userRepository.findByEmail(username).get().getAuthorities()
                            + "***************\n\n");

            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = tokenService.generateJwt(auth);

            return new LoginResponseDto(userRepository.findByEmail(username).get(), token);
        } catch (AuthenticationException e) {
            return new LoginResponseDto(null, ""); // maybe 40x error instead
        }
    }
}
