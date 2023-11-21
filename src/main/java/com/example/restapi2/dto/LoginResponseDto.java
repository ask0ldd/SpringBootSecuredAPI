package com.example.restapi2.dto;

import com.example.restapi2.models.User;

public class LoginResponseDto {
    private User user;
    private String jwt;

    public LoginResponseDto() {
        super();
    }

    public LoginResponseDto(User user, String jwt) {
        this.user = user;
        this.jwt = jwt;
    }

    public User getUser() {
        // get rid password
        User user = this.user;
        user.setPassword(null);
        return user;
    }

    public String getJwt() {
        return this.jwt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}
