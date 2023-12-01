package com.example.restapi2.dto;

import lombok.Builder;

@Builder
public class ReturnedUserDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;

    public ReturnedUserDto(Long userId, String firstname, String lastname, String email) {
        super();
        this.userId = userId;
        this.firstName = firstname;
        this.lastName = lastname;
        this.email = email;
    }
}
