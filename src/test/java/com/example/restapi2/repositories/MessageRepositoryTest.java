package com.example.restapi2.repositories;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.example.restapi2.models.Message;
import com.example.restapi2.models.Rental;
import com.example.restapi2.models.Role;
import com.example.restapi2.models.User;

@SpringBootTest(classes = { com.example.restapi2.Restapi2Application.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User user1;
    private User user2;
    private final Date date = new Date();
    private Rental rental1;
    private Rental rental2;

    @BeforeEach
    public void contextInit() {
        Role adminRole = roleRepository.save(new Role("ADMIN"));
        roleRepository.save(new Role("USER"));
        Role userRole = roleRepository.findByAuthority("USER").get();
        Set<Role> userAuthority = new HashSet<>();
        userAuthority.add(userRole);
        Set<Role> adminAuthority = new HashSet<>();
        adminAuthority.add(adminRole);
        userAuthority.add(userRole);

        User user1 = User.builder().userId(1L).firstname("Laurent").lastname("GINA")
                .email("laurentgina@mail.com").password("laurent").authorities(adminAuthority).build();
        User user2 = User.builder().userId(2L).firstname("Sophie").lastname("FONCEK")
                .email("sophiefoncek@mail.com").password("sophie").authorities(userAuthority).build();

        rental1 = Rental.builder().rentalId(1L).owner(user1).name("rental name 1")
                .description("rental description 1").picture("picture url 1").price(301F).surface(31F)
                .creation(date).update(date).build();
        rental2 = Rental.builder().rentalId(2L).owner(user2).name("rental name 2")
                .description("rental description 2").picture("picture url 2").price(302F).surface(32F)
                .creation(date).update(date).build();

        userRepository.save(user1);
        userRepository.save(user2);
        rentalRepository.save(rental1);
        rentalRepository.save(rental2);
    }

    @Test
    @DisplayName("Save() saves one Message into DB.")
    public void SaveMessage() {

        Message message = Message.builder().messageId(1L).rental(rental1).user(user1).message("my message")
                .creation(date).update(date).build();

        messageRepository.save(message);

        Optional<Message> collectedMessage = messageRepository.findById(1L);

        Assertions.assertThat(collectedMessage.get()).isNotNull();
        Assertions.assertThat(collectedMessage.get().getMessageId()).isGreaterThan(0);
        Assertions.assertThat(collectedMessage.get().getRental()).isEqualTo(rental1);
        Assertions.assertThat(collectedMessage.get().getUser()).isEqualTo(user1);
        Assertions.assertThat(collectedMessage.get().getMessage()).isEqualTo("my message");
    }

    @Test
    @DisplayName("FindAll() returns the 2 expected Messages.")
    public void FindAllMessages() {
        Message message1 = Message.builder().messageId(1L).rental(rental1).user(user1).message("my message 1")
                .creation(date).update(date).build();
        Message message2 = Message.builder().messageId(2L).rental(rental2).user(user2).message("my message 2")
                .creation(date).update(date).build();

        messageRepository.save(message1);
        messageRepository.save(message2);

        Iterable<Message> collectedMessages = messageRepository.findAll();

        Iterator<Message> it = collectedMessages.iterator();
        Message collectedMessage1 = it.next();
        Message collectedMessage2 = it.next();

        Assertions.assertThat(StreamSupport.stream(collectedMessages.spliterator(), false).count()).isEqualTo(2);

        Assertions.assertThat(collectedMessage1.getMessageId()).isGreaterThan(0);
        Assertions.assertThat(collectedMessage1.getRental()).isEqualTo(rental1);
        Assertions.assertThat(collectedMessage1.getUser()).isEqualTo(user1);
        Assertions.assertThat(collectedMessage1.getMessage()).isEqualTo("my message 1");

        Assertions.assertThat(collectedMessage2.getMessageId()).isGreaterThan(0);
        Assertions.assertThat(collectedMessage2.getRental()).isEqualTo(rental2);
        Assertions.assertThat(collectedMessage2.getUser()).isEqualTo(user2);
        Assertions.assertThat(collectedMessage2.getMessage()).isEqualTo("my message 2");
    }
}
