package com.example.restapi2.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi2.models.User;
import com.example.restapi2.services.AuthService;
import com.example.restapi2.services.TokenService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public User userRegister() {
        return authService.registerUser("", "");
    }

    /*
     * private static final Logger LOG =
     * LoggerFactory.getLogger(AuthController.class);
     * 
     * private TokenService tokenService;
     * 
     * public AuthController(TokenService tokenService) {
     * this.tokenService = tokenService;
     * }
     * 
     * @PostMapping("/token")
     * public String token(Authentication authentication) {
     * LOG.debug("Token requested for user : '{}'", authentication.getName());
     * String token = this.tokenService.generateToken(authentication);
     * LOG.debug("token granted : {}", token);
     * System.out.println(token);
     * return token;
     * }
     */
}
