package com.example.restapi2.dto;

import com.example.restapi2.models.User;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ReturnableUserDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;

    public ReturnableUserDto(User user) {
        super();
        this.userId = user.getUserId();
        this.firstName = user.getFirstname();
        this.lastName = user.getLastname();
        this.email = user.getEmail();
    }
}
