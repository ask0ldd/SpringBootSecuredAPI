package com.example.restapi2.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.restapi2.models.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

}
