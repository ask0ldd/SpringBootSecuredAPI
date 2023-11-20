package com.example.restapi2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restapi2.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // custom query
    Optional<User> findByEmail(String email);
}

// JPA Queries :
// https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html