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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi2.dto.ReturnableRentalDto;
import com.example.restapi2.models.Rental;
import com.example.restapi2.services.RentalService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping("/rentals")
    public ResponseEntity<?> getUsers() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Iterable<Rental> rentals = rentalService.getRentals();
            Iterable<ReturnableRentalDto> returnableRentals = StreamSupport.stream(rentals.spliterator(), false)
                    .map(rental -> {
                        ReturnableRentalDto returnableRental = new ReturnableRentalDto(rental);
                        return returnableRental;
                    })
                    .collect(Collectors.toList());
            return new ResponseEntity<>(returnableRentals, headers, HttpStatus.OK); // !!!
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find any Rental.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/rental/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") final Long id) {
        try {
            Rental rental = rentalService.getRental(id);
            return new ResponseEntity<>(new ReturnableRentalDto(rental), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested Rental.", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/rental/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") final Long id, @RequestBody Rental rental) {
        try {
            Rental currentRental = rentalService.getRental(id);

            String name = rental.getName();
            if (name != null) { // needs validation
                currentRental.setName(name);
            }

            String desc = rental.getDescription();
            if (desc != null) { // needs validation
                currentRental.setDescription(desc);
            }

            Rental modifiedRental = rentalService.saveRental(currentRental);

            System.out.println(modifiedRental);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(new ReturnableRentalDto(modifiedRental), headers,
                    HttpStatus.OK);
        } catch (Exception exception) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<String>("Can't find the requested Rental.", headers, HttpStatus.NOT_FOUND);
        }
    }

}
