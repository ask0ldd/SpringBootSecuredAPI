package com.example.restapi2.repositories;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.example.restapi2.models.Rental;
import com.example.restapi2.models.User;

@SpringBootTest(classes = { com.example.restapi2.Restapi2Application.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class RentalRepositoryTest {

        @Autowired
        private RentalRepository rentalRepository;

        private Date date;
        private Rental rental1;
        private Rental rental2;

        @BeforeEach
        public void init() {
                date = new Date();
                rental1 = new Rental(1L, 1L, "rental name 1", "rental description 1",
                                "picture url 1", 31F,
                                301F,
                                date,
                                date);
                rental2 = new Rental(2L, 2L, "rental name 2", "rental description 2",
                                "picture url 2", 32F,
                                302F,
                                date,
                                date);
        }

        @DisplayName("Save() saves one Rental into DB.")
        @Test
        public void SaveRental_ReturnSavedRentalFromDB() {

                rentalRepository.save(rental1);

                Optional<Rental> collectedRental = rentalRepository.findById(1L);

                Assertions.assertThat(collectedRental.get()).isNotNull();
                Assertions.assertThat(collectedRental.get().getRentalId()).isGreaterThan(0);
                Assertions.assertThat(collectedRental.get().getRentalId()).isEqualTo(rental1.getRentalId());
                Assertions.assertThat(collectedRental.get().getOwner()).isEqualTo(rental1.getOwner());
                Assertions.assertThat(collectedRental.get().getName()).isEqualTo(rental1.getName());
                Assertions.assertThat(collectedRental.get().getDescription()).isEqualTo(rental1.getDescription());
                Assertions.assertThat(collectedRental.get().getPicture()).isEqualTo(rental1.getPicture());
                Assertions.assertThat(collectedRental.get().getSurface()).isEqualTo(rental1.getSurface());
                Assertions.assertThat(collectedRental.get().getPrice()).isEqualTo(rental1.getPrice());
                /*
                 * Assertions.assertThat(collectedMessage.get().getCreation()).isEqualTo(date);
                 * Assertions.assertThat(collectedMessage.get().getUpdate()).isEqualTo(date);
                 */
        }

        @DisplayName("FindAll() returns the 2 expected Rentals")
        @Test
        public void FindAll_ReturnTwoSavedRentals() {

                rentalRepository.save(rental1);
                rentalRepository.save(rental2);

                Iterable<Rental> collectedRentals = rentalRepository.findAll();

                Iterator<Rental> it = collectedRentals.iterator();
                Rental collectedRental1 = it.next();
                Rental collectedRental2 = it.next();

                Assertions.assertThat(StreamSupport.stream(collectedRentals.spliterator(), false).count()).isEqualTo(2);
                Assertions.assertThat(collectedRental1).isNotNull();
                Assertions.assertThat(collectedRental1.getRentalId()).isEqualTo(1L);
                Assertions.assertThat(collectedRental1.getOwner()).isEqualTo(rental1.getOwner());
                Assertions.assertThat(collectedRental1.getName()).isEqualTo(rental1.getName());
                Assertions.assertThat(collectedRental1.getDescription()).isEqualTo(rental1.getDescription());
                Assertions.assertThat(collectedRental1.getPicture()).isEqualTo(rental1.getPicture());
                Assertions.assertThat(collectedRental1.getSurface()).isEqualTo(rental1.getSurface());
                Assertions.assertThat(collectedRental1.getPrice()).isEqualTo(rental1.getPrice());

                Assertions.assertThat(collectedRental2.getRentalId()).isEqualTo(2L);
                Assertions.assertThat(collectedRental2.getOwner()).isEqualTo(rental2.getOwner());
                Assertions.assertThat(collectedRental2.getName()).isEqualTo(rental2.getName());
                Assertions.assertThat(collectedRental2.getDescription()).isEqualTo(rental2.getDescription());
                Assertions.assertThat(collectedRental2.getPicture()).isEqualTo(rental2.getPicture());
                Assertions.assertThat(collectedRental2.getSurface()).isEqualTo(rental2.getSurface());
                Assertions.assertThat(collectedRental2.getPrice()).isEqualTo(rental2.getPrice());
        }

        @DisplayName("FindById() returns the expected Rental")
        @Test
        public void findById_ReturnOneTargetUser() {
                rentalRepository.save(rental1);
                Optional<Rental> collectedRental = rentalRepository.findById(1L);
                Assertions.assertThat(collectedRental.isPresent()).isTrue();
                Assertions.assertThat(collectedRental.get().getRentalId()).isGreaterThan(0);
                Assertions.assertThat(collectedRental.get().getName()).isEqualTo(rental1.getName());
                Assertions.assertThat(collectedRental.get().getDescription()).isEqualTo(rental1.getDescription());
                Assertions.assertThat(collectedRental.get().getPicture()).isEqualTo(rental1.getPicture());
                Assertions.assertThat(collectedRental.get().getPrice()).isEqualTo(rental1.getPrice());
                Assertions.assertThat(collectedRental.get().getSurface()).isEqualTo(rental1.getSurface());
                Assertions.assertThat(collectedRental.get().getOwner()).isEqualTo(rental1.getOwner());
        }

        @DisplayName("Delete() returns an empty optional")
        @Test
        public void delete_ReturnAnEmptyOptional() {
                rentalRepository.save(rental1);
                Optional<Rental> collectedRental = rentalRepository.findById(1L);
                Assertions.assertThat(collectedRental.isPresent()).isTrue();
                rentalRepository.deleteById(collectedRental.get().getRentalId());
                Optional<Rental> postDeletionCollectedRental = rentalRepository.findById(1L);
                Assertions.assertThat(postDeletionCollectedRental.isEmpty()).isTrue();
        }

        /*
         * @DisplayName("Update() returns the expected user")
         * 
         * @Test
         * public void update_ReturnAnEmptyOptional() {
         * userRepository.save(user1);
         * Optional<User> collectedUser = userRepository.findById(4L);
         * Assertions.assertThat(collectedUser.isPresent()).isTrue();
         * Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
         * Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user1.
         * getFirstname());
         * Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user1.
         * getLastname());
         * Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user1.
         * getPassword());
         * Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user1.
         * getEmail());
         * for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
         * Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
         * }
         * 
         * User targetUser = new User(4L, "updated firstname1", "updated lastname1",
         * "updatedemail1@domain.com",
         * "updated randomPassword1",
         * roleSet, new Date(), new Date());
         * 
         * userRepository.save(targetUser);
         * Optional<User> postUpdateCollectedUser = userRepository.findById(4L);
         * Assertions.assertThat(postUpdateCollectedUser.isPresent()).isTrue();
         * Assertions.assertThat(postUpdateCollectedUser.get().getFirstname()).
         * isEqualTo("updated firstname1");
         * Assertions.assertThat(postUpdateCollectedUser.get().getLastname()).
         * isEqualTo("updated lastname1");
         * Assertions.assertThat(postUpdateCollectedUser.get().getPassword()).
         * isEqualTo("updated randomPassword1");
         * Assertions.assertThat(postUpdateCollectedUser.get().getEmail()).isEqualTo(
         * "updatedemail1@domain.com");
         * }
         */
}