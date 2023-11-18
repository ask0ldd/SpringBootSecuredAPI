package com.example.restapi2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final RsaKeyProperties rsaKeys;

    public SecurityConfiguration(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    // Virtual userDetailsService
    @Bean
    public InMemoryUserDetailsManager user() {
        return new InMemoryUserDetailsManager(
                User.withUsername("ced").password("{noop}ced123").authorities("read").roles("ADMIN").build());
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build(); // !!! return jwtdecoder
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(authorize -> {
                    authorize
                            // .dispatcherTypeMatchers(DispatcherType.ERROR,
                            // DispatcherType.FORWARD).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/users**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/users**", "GET")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/test1**")).hasRole("ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/test2**")).hasRole("USER")
                            .requestMatchers(new AntPathRequestMatcher("/h2**")).permitAll()
                            .anyRequest().authenticated();
                    // .anyRequest().permitAll();
                }).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // basic popup login form
                .httpBasic(Customizer.withDefaults()).build();
        // html login
        // .formLogin(Customizer.withDefaults()).build();
    }

}

// MVC Request Matcher
// https://docs.spring.io/spring-security/reference/servlet/integrations/mvc.html
// Authorize HTTP Request
// https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#authorize-requests
// Basic Auth
// https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/basic.html