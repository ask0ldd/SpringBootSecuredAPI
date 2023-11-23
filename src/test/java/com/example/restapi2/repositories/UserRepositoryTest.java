package com.example.restapi2.repositories;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;

import com.example.restapi2.models.User;
import com.example.restapi2.models.Role;

// !!!!!!!!!!!! indicates which class os holding the context to load
@SpringBootTest(classes = { com.example.restapi2.Restapi2Application.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void SaveUser_ReturnSavedUserInDB() {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role(1, "ADMIN"));
        User user = new User(1L, "firstname", "lastname", "email@domain.com", "randomPassword",
                roleSet);
        userRepository.save(user);
        Optional<User> collectedUser = userRepository.findById(1L); // 1L = number is a Long

        Assertions.assertThat(collectedUser.get()).isNotNull();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(3);
        // Assertions.assertThat(collectedUser.get().getUserId()).isEqualTo(4);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo("firstname");
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo("lastname");
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo("randomPassword");
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo("email@domain.com");
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        } // !! to improve
    }

    @Test
    public void FindAll_ReturnFiveSavedUsers() {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role(1, "ADMIN"));
        User user1 = new User(null, "firstname1", "lastname1", "email1@domain.com", "randomPassword1",
                roleSet);
        User user2 = new User(null, "firstname2", "lastname2", "email2@domain.com", "randomPassword2",
                roleSet);
        userRepository.save(user1);
        userRepository.save(user2);
        Iterable<User> users = userRepository.findAll();
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(StreamSupport.stream(users.spliterator(), false).count()).isEqualTo(5);

    }

}