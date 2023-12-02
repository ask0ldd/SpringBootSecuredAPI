package com.example.restapi2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restapi2.exceptions.UserNotFoundException;
import com.example.restapi2.models.Message;
import com.example.restapi2.repositories.MessageRepository;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    public Iterable<Message> getMessages() {
        Iterable<Message> messages = messageRepository.findAll();
        if (!messages.iterator().hasNext())
            throw new UserNotFoundException("No message can be found.");
        return messages;
    }

    public Message getMessages(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target message can't be found."));
        return message;
    }
}
