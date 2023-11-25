package com.example.restapi2.services;

import static org.mockito.Mockito.when;

import java.util.Date;
// import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.GrantedAuthority;

import com.example.restapi2.models.Role;
import com.example.restapi2.models.User;
import com.example.restapi2.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final Set<Role> roleSet = Set.of(new Role(1, "ADMIN"));

    private final User user1 = new User(1L, "firstname", "lastname", "email@domain.com", "randomPassword",
            roleSet, new Date(), new Date());

    @Test
    @DisplayName("User exists : .getUser(id) should return the expected User")
    public void getUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        Optional<User> collectedUser = userService.getUser(1L);

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

    @Test
    @DisplayName("User doesn't exist : .getUser(id) should return an empty Optional")
    public void getMissingUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        Optional<User> collectedUser = userService.getUser(1L);

        Assertions.assertThat(collectedUser.isPresent()).isFalse();
        
    }

    @Test
    @DisplayName("User exists : .getUserByEmail(email) should return the expected User")
    public void getByUsername() {

        when(userRepository.findByEmail("email@domain.com")).thenReturn(Optional.ofNullable(user1));

        Optional<User> collectedUser = userService.getUserByEmail("email@domain.com");

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

    @Test
    @DisplayName("User doesn't exist : .getUserByEmail(email) should return an empty Optional")
    public void getMissingUserByUsername() {

        when(userRepository.findByEmail("email@domain.com")).thenReturn(Optional.ofNullable(null));

        Optional<User> collectedUser = userService.getUserByEmail("email@domain.com");

        Assertions.assertThat(collectedUser.isPresent()).isFalse();
    }

}
