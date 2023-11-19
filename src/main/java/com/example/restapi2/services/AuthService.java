package com.example.restapi2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restapi2.models.Role;
import com.example.restapi2.models.User;
import com.example.restapi2.repositories.UserRepository;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password) {

        String encodedPassword = passwordEncoder.encode(password);

        // !!! register user need frisntame / lastname / role to be added, take as
        // method parameters
        return userRepository.save(new User("temp", "temp", username,
                encodedPassword, Role.ADMIN));
    }
}
