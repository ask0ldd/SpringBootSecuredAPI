package com.example.restapi2.repositories;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;
// import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.example.restapi2.models.User;
import com.example.restapi2.models.Role;

@SpringBootTest(classes = { com.example.restapi2.Restapi2Application.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) // reinit context between each tests
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private Set<Role> roleSet = Set.of(new Role(1, "ADMIN"));
    private final User user1 = User.builder().userId(1L).firstname("Laurent").lastname("GINA")
            .email("laurentgina@mail.com").password("laurent").authorities(roleSet).build();
    private final User user2 = User.builder().userId(2L).firstname("Sophie").lastname("FONCEK")
            .email("sophiefoncek@mail.com").password("sophie").authorities(roleSet).build();
    private final User user3 = User.builder().userId(3L).firstname("Agathe").lastname("FEELING")
            .email("agathefeeling@mail.com").password("agathe").authorities(roleSet).build();
    private final User user6Invalid = User.builder().userId(6L).firstname("firstname2").lastname("lastname2")
            .email("laurentgina@mail.com").password("randomPassword2").authorities(roleSet).creation(new Date())
            .update(new Date()).build();
    private final User user4Invalid = User.builder().userId(4L).firstname("firstname2").lastname("lastname2")
            .email("laurentgina@mail.com").password("randomPassword2").authorities(roleSet).creation(new Date())
            .update(new Date()).build();
    private final User user1Replacement = User.builder().userId(1L).firstname("LaurentReplacement")
            .lastname("GINAReplacement")
            .email("laurentgina.replacement@mail.com").password("laurent").authorities(roleSet).build();

    @DisplayName("Save() saves one User into DB.")
    @Test
    public void saveUser_ReturnSavedUserFromDB() {
        Assertions.assertThat(userRepository.findById(user1.getUserId()).isPresent()).isFalse();

        userRepository.save(user1);
        // 4L since 3 users are created when initializing the context
        Optional<User> collectedUser = userRepository.findById(user1.getUserId());

        Assertions.assertThat(collectedUser.isPresent()).isTrue();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user1.getFirstname());
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user1.getLastname());
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user1.getEmail());
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        } // !! to improve
    }

    // !!! missing save() failure

    @DisplayName("FindAll() returns the 3 expected Users")
    @Test
    public void findAll_ReturnFiveSavedUsers() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        Iterable<User> users = userRepository.findAll();
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(StreamSupport.stream(users.spliterator(), false).count()).isEqualTo(3);
        List<String> emails = new ArrayList<String>();
        emails.add("laurentgina@mail.com");
        emails.add("sophiefoncek@mail.com");
        emails.add("agathefeeling@mail.com");

        int i = 0;
        for (Iterator<User> it = users.iterator(); it.hasNext(); i++) {
            User user = it.next();
            Assertions.assertThat(user.getEmail()).isEqualTo(emails.get(i));
        }
    }

    @DisplayName("FindById() returns the expected user")
    @Test
    public void findById_ReturnOneTargetUser() {
        userRepository.save(user1);
        Optional<User> collectedUser = userRepository.findById(user1.getUserId());
        Assertions.assertThat(collectedUser.isPresent()).isTrue();
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
    public void findByEmail_ReturnOneTargetUser() {
        userRepository.save(user1);
        Optional<User> collectedUser = userRepository.findByEmail(user1.getEmail());
        Assertions.assertThat(collectedUser.isPresent()).isTrue();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user1.getFirstname());
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user1.getLastname());
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user1.getEmail());
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        }
    }

    @DisplayName("Delete() returns an empty optional")
    @Test
    public void delete_ReturnAnEmptyOptional() {
        userRepository.save(user1);
        Optional<User> collectedUser = userRepository.findById(user1.getUserId());
        Assertions.assertThat(collectedUser.isPresent()).isTrue();
        userRepository.deleteById(collectedUser.get().getUserId());
        Optional<User> postDeletionCollectedUser = userRepository.findById(user1.getUserId());
        Assertions.assertThat(postDeletionCollectedUser.isEmpty()).isTrue();
    }

    @DisplayName("Update() replaces the expected user")
    @Test
    public void update_ReplaceTheExpectedUser() {
        userRepository.save(user1);
        Optional<User> collectedUser = userRepository.findById(user1.getUserId());
        Assertions.assertThat(collectedUser.isPresent()).isTrue();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user1.getFirstname());
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user1.getLastname());
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user1.getEmail());
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        }

        userRepository.save(user1Replacement);
        Optional<User> postUpdateCollectedUser = userRepository.findById(user1.getUserId());
        Assertions.assertThat(postUpdateCollectedUser.isPresent()).isTrue();
        Assertions.assertThat(postUpdateCollectedUser.get().getFirstname()).isEqualTo(user1Replacement.getFirstname());
        Assertions.assertThat(postUpdateCollectedUser.get().getLastname()).isEqualTo(user1Replacement.getLastname());
        Assertions.assertThat(postUpdateCollectedUser.get().getPassword()).isEqualTo(user1Replacement.getPassword());
        Assertions.assertThat(postUpdateCollectedUser.get().getEmail()).isEqualTo(user1Replacement.getEmail());
    }

    @DisplayName("Update() called with an invalid User")
    @Test
    public void update_ThrowsException() {
        userRepository.save(user1);
        Optional<User> collectedUser = userRepository.findById(1L);
        Assertions.assertThat(collectedUser.isPresent()).isTrue();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user4.getFirstname());
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user4.getLastname());
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user4.getPassword());
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user4.getEmail());

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user4Invalid);
        });

        Assertions.assertThat(exception.getMessage()).contains("Unique index or primary key violation");
    }

}