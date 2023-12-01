package com.example.restapi2.dto;

import com.example.restapi2.models.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ReturnableUserDto {

    private Long userId;
    private String firstname;
    private String lastname;
    private String email;

    public ReturnableUserDto(User user) {
        super();
        this.userId = user.getUserId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
    }
}
