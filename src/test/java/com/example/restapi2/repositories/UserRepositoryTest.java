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
import org.junit.jupiter.api.BeforeAll;
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

    private Set<Role> roleSet;
    private final User user1 = User.builder().userId(1L).firstname("Laurent").lastname("GINA")
            .email("laurentgina@mail.com").password("laurent").build();
    private final User user2 = User.builder().userId(2L).firstname("Sophie").lastname("FONCEK")
            .email("sophiefoncek@mail.com").password("sophie").build();
    private final User user3 = User.builder().userId(3L).firstname("Agathe").lastname("FEELING")
            .email("agathefeeling@mail.com").password("agathe").build();
    private User user4;
    private User user4Replacement;
    private User user5;
    private User user6Invalid;
    private User user4Invalid;

    @BeforeEach
    public void init() {
        roleSet = Set.of(new Role(1, "ADMIN"));
        user4 = User.builder().userId(4L).firstname("firstname1").lastname("lastname1")
                .email("email1@domain.com").password("randomPassword1").authorities(roleSet).creation(new Date())
                .update(new Date()).build();
        user4Replacement = new User(1L, "updated firstname3", "updated lastname3",
                "updatedemail3@domain.com",
                "randomPassword1", roleSet);
        user5 = User.builder().userId(5L).firstname("firstname2").lastname("lastname2")
                .email("email2@domain.com").password("randomPassword2").authorities(roleSet).creation(new Date())
                .update(new Date()).build();
        user6Invalid = User.builder().userId(6L).firstname("firstname2").lastname("lastname2")
                .email("laurentgina@mail.com").password("randomPassword2").authorities(roleSet).creation(new Date())
                .update(new Date()).build();
        user4Invalid = User.builder().userId(4L).firstname("firstname2").lastname("lastname2")
                .email("laurentgina@mail.com").password("randomPassword2").authorities(roleSet).creation(new Date())
                .update(new Date()).build();
    }

    @DisplayName("Save() saves one User into DB.")
    @Test
    public void saveUser_ReturnSavedUserFromDB() {
        Assertions.assertThat(userRepository.findById(4L).isPresent()).isFalse();

        userRepository.save(user4);
        // 4L since 3 users are created when initializing the context
        Optional<User> collectedUser = userRepository.findById(4L);

        Assertions.assertThat(collectedUser.isPresent()).isTrue();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user4.getFirstname());
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user4.getLastname());
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user4.getPassword());
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user4.getEmail());
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        } // !! to improve
    }

    // !!! missing save() failure

    @DisplayName("FindAll() returns the 5 expected Users")
    @Test
    public void findAll_ReturnFiveSavedUsers() {
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
    public void findById_ReturnOneTargetUser() {
        userRepository.save(user4);
        Optional<User> collectedUser = userRepository.findById(4L);
        Assertions.assertThat(collectedUser.isPresent()).isTrue();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user4.getFirstname());
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user4.getLastname());
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user4.getPassword());
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user4.getEmail());
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        }
    }

    @DisplayName("findByEmail() returns the expected user")
    @Test
    public void findByEmail_ReturnOneTargetUser() {
        userRepository.save(user4);
        Optional<User> collectedUser = userRepository.findByEmail("email1@domain.com");
        Assertions.assertThat(collectedUser.isPresent()).isTrue();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user4.getFirstname());
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user4.getLastname());
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user4.getPassword());
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user4.getEmail());
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        }
    }

    @DisplayName("Delete() returns an empty optional")
    @Test
    public void delete_ReturnAnEmptyOptional() {
        userRepository.save(user1);
        Optional<User> collectedUser = userRepository.findById(4L);
        Assertions.assertThat(collectedUser.isPresent()).isTrue();
        userRepository.deleteById(collectedUser.get().getUserId());
        Optional<User> postDeletionCollectedUser = userRepository.findById(4L);
        Assertions.assertThat(postDeletionCollectedUser.isEmpty()).isTrue();
    }

    @DisplayName("Update() replaces the expected user")
    @Test
    public void update_ReplaceTheExpectedUser() {
        userRepository.save(user1);
        Optional<User> collectedUser = userRepository.findById(4L);
        Assertions.assertThat(collectedUser.isPresent()).isTrue();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user1.getFirstname());
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user1.getLastname());
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user1.getEmail());
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        }

        User targetUser = new User(4L, "updated firstname1", "updated lastname1", "updatedemail1@domain.com",
                "updated randomPassword1",
                roleSet, new Date(), new Date());

        userRepository.save(targetUser);
        Optional<User> postUpdateCollectedUser = userRepository.findById(4L);
        Assertions.assertThat(postUpdateCollectedUser.isPresent()).isTrue();
        Assertions.assertThat(postUpdateCollectedUser.get().getFirstname()).isEqualTo("updated firstname1");
        Assertions.assertThat(postUpdateCollectedUser.get().getLastname()).isEqualTo("updated lastname1");
        Assertions.assertThat(postUpdateCollectedUser.get().getPassword()).isEqualTo("updated randomPassword1");
        Assertions.assertThat(postUpdateCollectedUser.get().getEmail()).isEqualTo("updatedemail1@domain.com");
    }

    @DisplayName("Update() called with an invalid User")
    @Test
    public void update_ThrowsException() {
        userRepository.save(user4);
        Optional<User> collectedUser = userRepository.findById(1L);
        Assertions.assertThat(collectedUser.isPresent()).isTrue();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo(user1.getFirstname());
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo(user1.getLastname());
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo(user1.getEmail());

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user4Invalid);
        });

        Assertions.assertThat(exception.getMessage()).contains("Unique index or primary key violation");
    }

}