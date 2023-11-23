package com.example.restapi2.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
@Builder
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "sender")
    private Long sender;

    @Column(name = "recipient")
    private Long recipient;

    @Column(name = "body")
    private String body;

    /*
     * public Message(Long messageId, Long sender, Long recipient, String body) {
     * this.messageId = messageId;
     * this.sender = sender;
     * this.recipient = recipient;
     * this.body = body;
     * }
     */

}
