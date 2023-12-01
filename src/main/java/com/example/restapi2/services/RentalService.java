package com.example.restapi2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restapi2.exceptions.UserNotFoundException;
import com.example.restapi2.models.Rental;
import com.example.restapi2.repositories.RentalRepository;

import lombok.Data;

@Data
@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    public Rental getRental(final Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target rental can't be found."));
        return rental;
    }

    public Iterable<Rental> getRentals() {
        Iterable<Rental> rentals = rentalRepository.findAll();
        if (!rentals.iterator().hasNext())
            throw new UserNotFoundException("Can't find any Rental.");
        return rentals;
    }

    public Rental saveRental(Rental rental) {
        Rental savedRental = rentalRepository.save(rental);
        return savedRental;
    }

    public void deleteUser(final Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target rental can't be found."));
        rentalRepository.delete(rental);
    }
}
