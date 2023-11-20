package com.example.restapi2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
// This publishes Spring Security’s default Filter chain as a @Bean
public class SecurityConfiguration {

    private final RsaKeyProperties rsaKeys;

    public SecurityConfiguration(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    // Virtual userDetailsService
    /*
     * @Bean
     * public InMemoryUserDetailsManager user() {
     * return new InMemoryUserDetailsManager(
     * User.withUsername("ced").password("{noop}ced123").authorities("read").roles(
     * "ADMIN").build());
     * }
     */

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build(); // !!! return jwtdecoder
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(daoProvider);
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
                            // .requestMatchers(new AntPathRequestMatcher("/token**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/users**", "GET")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/test1**")).hasRole("ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/test2**")).hasRole("USER")
                            .requestMatchers(new AntPathRequestMatcher("/h2-console/*")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/auth/*")).permitAll()
                            .anyRequest().authenticated();
                })
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // basic popup login form
                .httpBasic(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions().disable()) // allows h2-console frames to be displayed
                .build();
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

/*
 * 
 * @Configuration
 * 
 * @EnableWebSecurity
 * public class SessionManagementSecurityConfig {
 * 
 * @Bean
 * public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
 * Exception {
 * http
 * .authorizeRequests((authorizeRequests) ->
 * authorizeRequests
 * .anyRequest().hasRole("USER")
 * )
 * .formLogin((formLogin) ->
 * formLogin
 * .permitAll()
 * )
 * .sessionManagement((sessionManagement) ->
 * sessionManagement
 * .sessionConcurrency((sessionConcurrency) ->
 * sessionConcurrency
 * .maximumSessions(1)
 * .expiredUrl("/login?expired")
 * )
 * );
 * return http.build();
 * }
 * 
 * @Bean
 * public UserDetailsService userDetailsService() {
 * UserDetails user = User.withDefaultPasswordEncoder()
 * .username("user")
 * .password("password")
 * .roles("USER")
 * .build();
 * return new InMemoryUserDetailsManager(user);
 * }
 * }
 * 
 */