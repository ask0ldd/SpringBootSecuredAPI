package com.example.restapi2.repositories;

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

import com.example.restapi2.models.Message;
import com.example.restapi2.models.Rental;

@SpringBootTest(classes = { com.example.restapi2.Restapi2Application.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    @DisplayName("Save() saves one Message into DB.")
    public void SaveMessage() {
        Message message = new Message(1L, 1L, 2L, "my message");

        messageRepository.save(message);

        Optional<Message> collectedMessage = messageRepository.findById(1L);

        Assertions.assertThat(collectedMessage.get()).isNotNull();
        Assertions.assertThat(collectedMessage.get().getMessageId()).isGreaterThan(0);
        Assertions.assertThat(collectedMessage.get().getSender()).isEqualTo(1L);
        Assertions.assertThat(collectedMessage.get().getRecipient()).isEqualTo(2L);
        Assertions.assertThat(collectedMessage.get().getBody()).isEqualTo("my message");
    }

    @Test
    @DisplayName("FindAll() returns the 2 expected Messages.")
    public void FindAllMessages() {
        Message message1 = new Message(1L, 1L, 3L, "my message 1");
        Message message2 = new Message(2L, 2L, 4L, "my message 2");

        messageRepository.save(message1);
        messageRepository.save(message2);

        Iterable<Message> collectedMessages = messageRepository.findAll();

        Iterator<Message> it = collectedMessages.iterator();
        Message collectedMessage1 = it.next();
        Message collectedMessage2 = it.next();

        Assertions.assertThat(StreamSupport.stream(collectedMessages.spliterator(), false).count()).isEqualTo(2);

        Assertions.assertThat(collectedMessage1.getMessageId()).isGreaterThan(0);
        Assertions.assertThat(collectedMessage1.getSender()).isEqualTo(1L);
        Assertions.assertThat(collectedMessage1.getRecipient()).isEqualTo(3L);
        Assertions.assertThat(collectedMessage1.getBody()).isEqualTo("my message 1");

        Assertions.assertThat(collectedMessage2.getMessageId()).isGreaterThan(0);
        Assertions.assertThat(collectedMessage2.getSender()).isEqualTo(2L);
        Assertions.assertThat(collectedMessage2.getRecipient()).isEqualTo(4L);
        Assertions.assertThat(collectedMessage2.getBody()).isEqualTo("my message 2");
    }
}
