package com.example.restapi2.dto;

import com.example.restapi2.models.Rental;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ReturnableRentalDto {
    private Long rentalId;
    private String description;
    private String name;
    private ReturnableUserDto owner;
    private String picture;
    private Float surface;
    private Float price;

    public ReturnableRentalDto(Rental rental) {
        rentalId = rental.getRentalId();
        description = rental.getDescription();
        name = rental.getName();
        picture = rental.getPicture();
        surface = rental.getSurface();
        price = rental.getPrice();
        owner = new ReturnableUserDto(rental.getOwner());
    }

}
