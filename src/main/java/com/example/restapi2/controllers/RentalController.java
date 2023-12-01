package com.example.restapi2.controllers;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi2.dto.ReturnableUserDto;
import com.example.restapi2.models.Rental;
import com.example.restapi2.services.RentalService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "Authorization")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping("/rentals")
    public ResponseEntity<?> getUsers() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Iterable<Rental> users = rentalService.getRentals();
            return new ResponseEntity<>(users, headers, HttpStatus.OK); // !!!
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find any Rental.", HttpStatus.NOT_FOUND);
        }
    }
}
