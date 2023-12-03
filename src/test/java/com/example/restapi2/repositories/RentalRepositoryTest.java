package com.example.restapi2.repositories;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.example.restapi2.models.Rental;
import com.example.restapi2.models.Role;
import com.example.restapi2.models.User;

@SpringBootTest(classes = { com.example.restapi2.Restapi2Application.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class RentalRepositoryTest {

        @Autowired
        private RentalRepository rentalRepository;

        @Autowired
        private UserRepository userRepository;

        private Date date;
        private Rental rental1;
        private Rental rental2;
        private Rental rental1Replacement;
        private Set<Role> roleSet = Set.of(new Role(1, "ADMIN"));
        private final User user1 = User.builder().userId(1L).firstname("Laurent").lastname("GINA")
                        .email("laurentgina@mail.com").password("laurent").authorities(roleSet).build();
        private final User user2 = User.builder().userId(2L).firstname("Sophie").lastname("FONCEK")
                        .email("sophiefoncek@mail.com").password("sophie").authorities(roleSet).build();
        private final User user3 = User.builder().userId(3L).firstname("Agathe").lastname("FEELING")
                        .email("agathefeeling@mail.com").password("agathe").authorities(roleSet).build();

        @BeforeEach
        public void init() {
                userRepository.save(user1);
                userRepository.save(user2);
                userRepository.save(user3);
                date = new Date();
                rental1 = Rental.builder().rentalId(1L).owner(user1).name("rental name 1")
                                .description("rental description 1").picture("picture url 1").price(301F).surface(31F)
                                .creation(date).update(date).build();
                rental2 = Rental.builder().rentalId(2L).owner(user2).name("rental name 2")
                                .description("rental description 2").picture("picture url 2").price(302F).surface(32F)
                                .creation(date).update(date).build();
                rental1Replacement = Rental.builder().rentalId(1L).owner(user3).name("rental name 3")
                                .description("rental description 3").picture("picture url 3").price(303F).surface(33F)
                                .creation(date).update(date).build();
        }

        @DisplayName("Save() saves one Rental into DB.")
        @Test
        public void saveRental_ReturnSavedRental() {

                rentalRepository.save(rental1);

                Optional<Rental> collectedRental = rentalRepository.findById(1L);

                Assertions.assertThat(collectedRental.get()).isNotNull();
                Assertions.assertThat(collectedRental.get().getRentalId()).isGreaterThan(0);
                Assertions.assertThat(collectedRental.get().getRentalId()).isEqualTo(rental1.getRentalId());
                Assertions.assertThat(collectedRental.get().getOwner().getUserId()).isEqualTo(user1.getUserId());
                Assertions.assertThat(collectedRental.get().getName()).isEqualTo(rental1.getName());
                Assertions.assertThat(collectedRental.get().getDescription()).isEqualTo(rental1.getDescription());
                Assertions.assertThat(collectedRental.get().getPicture()).isEqualTo(rental1.getPicture());
                Assertions.assertThat(collectedRental.get().getSurface()).isEqualTo(rental1.getSurface());
                Assertions.assertThat(collectedRental.get().getPrice()).isEqualTo(rental1.getPrice());
        }

        @DisplayName("FindAll() returns the 2 expected Rentals")
        @Test
        public void findAll_ReturnTwoSavedRentals() {

                rentalRepository.save(rental1);
                rentalRepository.save(rental2);

                Iterable<Rental> collectedRentals = rentalRepository.findAll();

                Iterator<Rental> it = collectedRentals.iterator();
                Rental collectedRental1 = it.next();
                Rental collectedRental2 = it.next();

                Assertions.assertThat(StreamSupport.stream(collectedRentals.spliterator(), false).count()).isEqualTo(2);
                Assertions.assertThat(collectedRental1).isNotNull();
                Assertions.assertThat(collectedRental1.getRentalId()).isEqualTo(1L);
                Assertions.assertThat(collectedRental1.getOwner().getUserId())
                                .isEqualTo(rental1.getOwner().getUserId());
                Assertions.assertThat(collectedRental1.getName()).isEqualTo(rental1.getName());
                Assertions.assertThat(collectedRental1.getDescription()).isEqualTo(rental1.getDescription());
                Assertions.assertThat(collectedRental1.getPicture()).isEqualTo(rental1.getPicture());
                Assertions.assertThat(collectedRental1.getSurface()).isEqualTo(rental1.getSurface());
                Assertions.assertThat(collectedRental1.getPrice()).isEqualTo(rental1.getPrice());

                Assertions.assertThat(collectedRental2.getRentalId()).isEqualTo(2L);
                Assertions.assertThat(collectedRental2.getOwner().getUserId())
                                .isEqualTo(rental2.getOwner().getUserId());
                Assertions.assertThat(collectedRental2.getName()).isEqualTo(rental2.getName());
                Assertions.assertThat(collectedRental2.getDescription()).isEqualTo(rental2.getDescription());
                Assertions.assertThat(collectedRental2.getPicture()).isEqualTo(rental2.getPicture());
                Assertions.assertThat(collectedRental2.getSurface()).isEqualTo(rental2.getSurface());
                Assertions.assertThat(collectedRental2.getPrice()).isEqualTo(rental2.getPrice());
        }

        @DisplayName("FindById() returns the expected Rental")
        @Test
        public void findById_ReturnOneTargetRental() {
                rentalRepository.save(rental1);
                Optional<Rental> collectedRental = rentalRepository.findById(1L);
                Assertions.assertThat(collectedRental.isPresent()).isTrue();
                Assertions.assertThat(collectedRental.get().getRentalId()).isGreaterThan(0);
                Assertions.assertThat(collectedRental.get().getName()).isEqualTo(rental1.getName());
                Assertions.assertThat(collectedRental.get().getDescription()).isEqualTo(rental1.getDescription());
                Assertions.assertThat(collectedRental.get().getPicture()).isEqualTo(rental1.getPicture());
                Assertions.assertThat(collectedRental.get().getPrice()).isEqualTo(rental1.getPrice());
                Assertions.assertThat(collectedRental.get().getSurface()).isEqualTo(rental1.getSurface());
                Assertions.assertThat(collectedRental.get().getOwner().getUserId())
                                .isEqualTo(rental1.getOwner().getUserId());
        }

        @DisplayName("Delete() returns an empty Optional")
        @Test
        public void delete_ReturnAnEmptyOptional() {
                rentalRepository.save(rental1);
                Optional<Rental> collectedRental = rentalRepository.findById(1L);
                Assertions.assertThat(collectedRental.isPresent()).isTrue();
                rentalRepository.deleteById(collectedRental.get().getRentalId());
                Optional<Rental> postDeletionCollectedRental = rentalRepository.findById(1L);
                Assertions.assertThat(postDeletionCollectedRental.isEmpty()).isTrue();
        }

        @DisplayName("Update() replaces the expected Rental")
        @Test
        public void update_ReplaceTheExpectedRental() {
                rentalRepository.save(rental1);
                Optional<Rental> collectedRental = rentalRepository.findById(1L);
                Assertions.assertThat(collectedRental.get().getRentalId()).isGreaterThan(0);
                Assertions.assertThat(collectedRental.get().getName()).isEqualTo(rental1.getName());
                Assertions.assertThat(collectedRental.get().getDescription()).isEqualTo(rental1.getDescription());
                Assertions.assertThat(collectedRental.get().getPicture()).isEqualTo(rental1.getPicture());
                Assertions.assertThat(collectedRental.get().getPrice()).isEqualTo(rental1.getPrice());
                Assertions.assertThat(collectedRental.get().getSurface()).isEqualTo(rental1.getSurface());
                Assertions.assertThat(collectedRental.get().getOwner().getUserId())
                                .isEqualTo(rental1.getOwner().getUserId());

                rentalRepository.save(rental1Replacement);
                Optional<Rental> postUpdateCollectedRental = rentalRepository.findById(1L);
                Assertions.assertThat(postUpdateCollectedRental.get().getRentalId()).isGreaterThan(0);
                Assertions.assertThat(postUpdateCollectedRental.get().getName())
                                .isEqualTo(rental1Replacement.getName());
                Assertions.assertThat(postUpdateCollectedRental.get().getDescription())
                                .isEqualTo(rental1Replacement.getDescription());
                Assertions.assertThat(postUpdateCollectedRental.get().getPicture())
                                .isEqualTo(rental1Replacement.getPicture());
                Assertions.assertThat(postUpdateCollectedRental.get().getPrice())
                                .isEqualTo(rental1Replacement.getPrice());
                Assertions.assertThat(postUpdateCollectedRental.get().getSurface())
                                .isEqualTo(rental1Replacement.getSurface());
                Assertions.assertThat(postUpdateCollectedRental.get().getOwner().getUserId())
                                .isEqualTo(rental1Replacement.getOwner().getUserId());

        }
}