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

        Rental rental1 = new Rental(1L, user1, "rental name 1", "rental description 1",
                "picture url 1", 31F, 301F, date, date);
        Rental rental2 = new Rental(2L, user2, "rental name 2", "rental description 2",
                "picture url 2", 32F, 302F, date, date);

        userRepository.save(user1);
        userRepository.save(user2);
        rentalRepository.save(rental1);
        rentalRepository.save(rental2);
    }

    @Test
    @DisplayName("Save() saves one Message into DB.")
    public void SaveMessage() {
        Message message = new Message(1L, rental1, user1, "my message", date, date);

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
        Message message1 = new Message(1L, rental1, user1, "my message 1", date, date);
        Message message2 = new Message(2L, rental2, user2, "my message 2", date, date);

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
