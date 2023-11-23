package com.example.restapi2.repositories;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.restapi2.models.Message;

@SpringBootTest(classes = { com.example.restapi2.Restapi2Application.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void SaveMessage_ReturnSavedMessageFromDB() {
        Message message = new Message(1L, 1L, 2L, "my message");

        messageRepository.save(message);

        Optional<Message> collectedMessage = messageRepository.findById(1L);

        Assertions.assertThat(collectedMessage.get()).isNotNull();
        Assertions.assertThat(collectedMessage.get().getMessageId()).isGreaterThan(0);
        Assertions.assertThat(collectedMessage.get().getSender()).isEqualTo(1L);
        Assertions.assertThat(collectedMessage.get().getRecipient()).isEqualTo(2L);
        Assertions.assertThat(collectedMessage.get().getBody()).isEqualTo("my message");

    }
}
