package com.example.restapi2.repositories;

import java.util.HashSet;
import java.util.Set;

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

// !!!!!!!!!!!! indicates which class to load the context from
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
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        // Assertions.assertThat(collectedUser.get().getUserId()).isEqualTo(4);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo("firstname");
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo("lastname");
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo("randomPassword");
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo("email@domain.com");
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        }
    }

}

// public User(Long userId, String firstName, String lastName, String email,
// String password, authorities)