package com.example.restapi2.repositories;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.restapi2.models.Rental;

@SpringBootTest(classes = { com.example.restapi2.Restapi2Application.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;

    @DisplayName("Save() saves one Rental into DB.")
    @Test
    public void SaveRental_ReturnSavedRentalFromDB() {
        Date date = new Date();
        Rental rental = new Rental(1L, 1L, "rental name", "rental description", "picture url", 30, 300F, date,
                date);

        rentalRepository.save(rental);

        Optional<Rental> collectedMessage = rentalRepository.findById(1L);

        Assertions.assertThat(collectedMessage.get()).isNotNull();
        Assertions.assertThat(collectedMessage.get().getRentalId()).isGreaterThan(0);
        Assertions.assertThat(collectedMessage.get().getRentalId()).isEqualTo(1L);
        Assertions.assertThat(collectedMessage.get().getOwner()).isEqualTo(1L);
        Assertions.assertThat(collectedMessage.get().getName()).isEqualTo("rental name");
        Assertions.assertThat(collectedMessage.get().getDescription()).isEqualTo("rental description");
        Assertions.assertThat(collectedMessage.get().getPicture()).isEqualTo("picture url");
        Assertions.assertThat(collectedMessage.get().getSurface()).isEqualTo(30);
        Assertions.assertThat(collectedMessage.get().getPrice()).isEqualTo(300F);
        /*
         * Assertions.assertThat(collectedMessage.get().getCreation()).isEqualTo(date);
         * Assertions.assertThat(collectedMessage.get().getUpdate()).isEqualTo(date);
         */
    }

    @DisplayName("FindAll() returns the 2 expected Rentals")
    @Test
    public void FindAll_ReturnTwoSavedRentals() {
        Date date = new Date();
        Rental rental1 = new Rental(1L, 1L, "rental name 1", "rental description 1", "picture url 1", 31, 301F, date,
                date);
        Rental rental2 = new Rental(2L, 2L, "rental name 2", "rental description 2", "picture url 2", 32, 302F, date,
                date);

        rentalRepository.save(rental1);
        rentalRepository.save(rental2);

        Iterable<Rental> collectedRentals = rentalRepository.findAll();

        Iterator<Rental> it = collectedRentals.iterator();
        Rental collectedRental1 = it.next();
        Rental collectedRental2 = it.next();

        Assertions.assertThat(StreamSupport.stream(collectedRentals.spliterator(), false).count()).isEqualTo(2);
        Assertions.assertThat(collectedRental1.getRentalId()).isEqualTo(1L);
        Assertions.assertThat(collectedRental1.getOwner()).isEqualTo(1L);
        Assertions.assertThat(collectedRental1.getName()).isEqualTo("rental name 1");
        Assertions.assertThat(collectedRental1.getDescription()).isEqualTo("rental description 1");
        Assertions.assertThat(collectedRental1.getPicture()).isEqualTo("picture url 1");
        Assertions.assertThat(collectedRental1.getSurface()).isEqualTo(31);
        Assertions.assertThat(collectedRental1.getPrice()).isEqualTo(301F);

        Assertions.assertThat(collectedRental2.getRentalId()).isEqualTo(2L);
        Assertions.assertThat(collectedRental2.getOwner()).isEqualTo(2L);
        Assertions.assertThat(collectedRental2.getName()).isEqualTo("rental name 2");
        Assertions.assertThat(collectedRental2.getDescription()).isEqualTo("rental description 2");
        Assertions.assertThat(collectedRental2.getPicture()).isEqualTo("picture url 2");
        Assertions.assertThat(collectedRental2.getSurface()).isEqualTo(32);
        Assertions.assertThat(collectedRental2.getPrice()).isEqualTo(302F);
    }
}