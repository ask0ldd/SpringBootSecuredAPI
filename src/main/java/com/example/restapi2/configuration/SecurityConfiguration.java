package com.example.restapi2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/users**").permitAll();
                }).build();

    }
}

// MVC Request Matcher
// https://docs.spring.io/spring-security/reference/servlet/integrations/mvc.html
// Authorize HTTP Request
// https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#authorize-requests
// Basic Auth
// https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/basic.html