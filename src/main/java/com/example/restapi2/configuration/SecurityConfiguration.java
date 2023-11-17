package com.example.restapi2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(authorize -> {
                    authorize.dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/users**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/users**", "GET")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/test**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/h2**")).permitAll()
                            .anyRequest().authenticated();
                    // .anyRequest().permitAll();
                }).formLogin(Customizer.withDefaults()).build();
    }
}

// MVC Request Matcher
// https://docs.spring.io/spring-security/reference/servlet/integrations/mvc.html
// Authorize HTTP Request
// https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#authorize-requests
// Basic Auth
// https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/basic.html