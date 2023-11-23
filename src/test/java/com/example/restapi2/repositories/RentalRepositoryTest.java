package com.example.restapi2.repositories;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.restapi2.models.Rental;

import java.util.Date;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;

@SpringBootTest(classes = { com.example.restapi2.Restapi2Application.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Test
    public void SaveRental_ReturnSavedRentalFromDB() {
        Rental rental = new Rental(1L, 1L, "rental name", "rental description", "picture url", 30, 300F, new Date(),
                new Date());

        rentalRepository.save(rental);

        Optional<Rental> collectedMessage = rentalRepository.findById(1L);

        Assertions.assertThat(collectedMessage.get()).isNotNull();
        Assertions.assertThat(collectedMessage.get().getRentalId()).isGreaterThan(0);
    }
}

/*
 * 
 * @Column(name = "rental_id")
 * private Long rentalId;
 * 
 * @Column(name = "owner")
 * private Integer owner;
 * 
 * @Column(name = "name")
 * private String name;
 * 
 * @Column(name = "description")
 * private String description;
 * 
 * @Column(name = "picture")
 * private String picture;
 * 
 * @Column(name = "surface")
 * private Integer surface;
 * 
 * @Column(name = "price")
 * private Float price;
 * 
 * @Column(name = "creation")
 * private Date creation;
 * 
 * @Column(name = "update")
 * private Date update;
 */