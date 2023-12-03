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

    private final Date date = new Date();
    private Rental rental1;
    private Rental rental2;

    private Set<Role> roleSet = Set.of(new Role(1, "ADMIN"));
    private final User user1 = User.builder().userId(1L).firstname("Laurent").lastname("GINA")
            .email("laurentgina@mail.com").password("laurent").authorities(roleSet).creation(date).update(date).build();
    private final User user2 = User.builder().userId(2L).firstname("Sophie").lastname("FONCEK")
            .email("sophiefoncek@mail.com").password("sophie").authorities(roleSet).creation(date).update(date).build();
    private final User user3 = User.builder().userId(3L).firstname("Agathe").lastname("FEELING")
            .email("agathefeeling@mail.com").password("agathe").authorities(roleSet).creation(date).update(date)
            .build();

    @BeforeEach
    public void init() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        rental1 = Rental.builder().rentalId(1L).owner(user1).name("rental name 1")
                .description("rental description 1").picture("picture url 1").price(301F).surface(31F)
                .creation(date).update(date).build();
        rental2 = Rental.builder().rentalId(2L).owner(user2).name("rental name 2")
                .description("rental description 2").picture("picture url 2").price(302F).surface(32F)
                .creation(date).update(date).build();

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
        Assertions.assertThat(collectedMessage.get().getUser().getUserId()).isEqualTo(user1.getUserId());
        Assertions.assertThat(collectedMessage.get().getRental().getRentalId()).isEqualTo(rental1.getRentalId());
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
        Assertions.assertThat(collectedMessage1.getRental().getRentalId()).isEqualTo(rental1.getRentalId());
        Assertions.assertThat(collectedMessage1.getUser().getUserId()).isEqualTo(user1.getUserId());
        Assertions.assertThat(collectedMessage1.getMessage()).isEqualTo("my message 1");

        Assertions.assertThat(collectedMessage2.getMessageId()).isGreaterThan(0);
        Assertions.assertThat(collectedMessage2.getRental().getRentalId()).isEqualTo(rental2.getRentalId());
        Assertions.assertThat(collectedMessage2.getUser().getUserId()).isEqualTo(user2.getUserId());
        Assertions.assertThat(collectedMessage2.getMessage()).isEqualTo("my message 2");
    }
}
