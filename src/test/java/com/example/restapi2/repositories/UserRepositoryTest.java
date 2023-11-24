package com.example.restapi2.repositories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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

    private final Set<Role> roleSet = Set.of(new Role(1, "ADMIN"));

    private final User user1 = new User(null, "firstname1", "lastname1", "email1@domain.com", "randomPassword1",
            roleSet);

    private final User user2 = new User(null, "firstname2", "lastname2", "email2@domain.com", "randomPassword2",
            roleSet);

    @DisplayName("Save() saves one User into DB.")
    @Test
    public void SaveUser_ReturnSavedUserFromDB() {

        userRepository.save(user1);
        // 4L since 3 users are created when initializing the context
        Optional<User> collectedUser = userRepository.findById(4L);

        Assertions.assertThat(collectedUser.get()).isNotNull();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user1.getFirstname());
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user1.getLastname());
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user1.getEmail());
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        } // !! to improve
    }

    @DisplayName("FindAll() returns the 5 expected Users")
    @Test
    public void FindAll_ReturnFiveSavedUsers() {
        userRepository.save(user1);
        userRepository.save(user2);
        Iterable<User> users = userRepository.findAll();
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(StreamSupport.stream(users.spliterator(), false).count()).isEqualTo(5);
        List<String> emails = new ArrayList<String>();
        emails.add("laurentgina@mail.com");
        emails.add("sophiefoncek@mail.com");
        emails.add("agathefeeling@mail.com");
        emails.add("email1@domain.com");
        emails.add("email2@domain.com");

        int i = 0;
        for (Iterator<User> it = users.iterator(); it.hasNext(); i++) {
            User user = it.next();
            Assertions.assertThat(user.getEmail()).isEqualTo(emails.get(i));
        }
    }

    @DisplayName("FindById() returns the expected user")
    @Test
    public void FindById_ReturnOneTargetUser() {
        userRepository.save(user1);
        Optional<User> collectedUser = userRepository.findById(4L);
        Assertions.assertThat(collectedUser.get()).isNotNull();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user1.getFirstname());
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user1.getLastname());
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user1.getEmail());
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        }
    }

    @DisplayName("findByEmail() returns the expected user")
    @Test
    public void FindByEmail_ReturnOneTargetUser() {
        userRepository.save(user1);
        Optional<User> collectedUser = userRepository.findByEmail("email1@domain.com");
        Assertions.assertThat(collectedUser.get()).isNotNull();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user1.getFirstname());
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user1.getLastname());
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user1.getEmail());
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        }
    }

}