package com.example.restapi2.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.restapi2.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

// @Configuration
// @RequiredArgsConstructor
public class AppConfiguration {

    // @Autowired
    private UserRepository userRepository;

    // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/user-details-service.html
    // @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                System.out.println("***************USER : " + userRepository.findByEmail(username).get().getUsername());
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found."));
                // orElseThrow related to optional<> (userRepository.findByEmail(username)
                // return an optional)

            }
        };
    }
}
