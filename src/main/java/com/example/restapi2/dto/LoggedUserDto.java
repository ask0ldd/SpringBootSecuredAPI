package com.example.restapi2.dto;

import com.example.restapi2.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoggedUserDto { // return a simplified version of the user model
    private Long userId;
    private String firstname;
    private String lastname;
    private String email;

    public LoggedUserDto(User user) {
        this.userId = user.getUserId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
    }
}
