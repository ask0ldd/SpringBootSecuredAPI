package com.example.restapi2.repositories;

import java.util.Date;
import java.util.Optional;
import org.assertj.core.api.Assertions;
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
}