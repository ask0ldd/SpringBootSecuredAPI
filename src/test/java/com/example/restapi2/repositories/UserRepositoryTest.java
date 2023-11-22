package com.example.restapi2.repositories;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.restapi2.models.User;
import com.example.restapi2.models.Role;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void SaveAll_ReturnSavedUser() {
        Set<Role> typedSet = new HashSet<>();
        typedSet.add(new Role(1, "ADMIN"));
        User user = new User(1L, "firstname", "lastname", "email@domain.com", "randomPassword",
                typedSet);
        userRepository.save(user);
        Optional<User> collectedUser = userRepository.findById(1L);
    }
}

// public User(Long userId, String firstName, String lastName, String email,
// String password, authorities)