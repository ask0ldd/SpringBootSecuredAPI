package com.example.restapi2.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.restapi2.exceptions.UserNotFoundException;
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

        User collectedUser = userService.getUser(1L);

        Assertions.assertThat(collectedUser).isNotNull();
        Assertions.assertThat(collectedUser.getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.getFirstname()).isEqualTo(user1.getFirstname());
        Assertions.assertThat(collectedUser.getLastname()).isEqualTo(user1.getLastname());
        Assertions.assertThat(collectedUser.getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(collectedUser.getEmail()).isEqualTo(user1.getEmail());
        for (GrantedAuthority role : collectedUser.getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        } // !! to improve
    }

    @Test
    @DisplayName("User doesn't exist : .getUser(id) should throw")
    public void getMissingUser() {

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(UserNotFoundException.class, () -> { 
            userService.getUser(1L);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("Target user can't be found.");
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        
    }

    @Test
    @DisplayName("User exists : .getUserByEmail(email) should return the expected User")
    public void getByUsername() {

        when(userRepository.findByEmail("email@domain.com")).thenReturn(Optional.ofNullable(user1));

        User collectedUser = userService.getUserByEmail("email@domain.com");

        Assertions.assertThat(collectedUser).isNotNull();
        Assertions.assertThat(collectedUser.getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.getFirstname()).isEqualTo(user1.getFirstname());
        Assertions.assertThat(collectedUser.getLastname()).isEqualTo(user1.getLastname());
        Assertions.assertThat(collectedUser.getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(collectedUser.getEmail()).isEqualTo(user1.getEmail());
        for (GrantedAuthority role : collectedUser.getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        } // !! to improve
    }

    @Test
    @DisplayName("User doesn't exist : .getUserByEmail(email) should throw")
    public void getMissingUserByUsername() {

        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(UserNotFoundException.class, () -> { 
            userService.getUserByEmail("email@domain.com");
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("Target user can't be found.");
        verify(userRepository, times(1)).findByEmail(Mockito.anyString());
    }

    @Test
    @DisplayName("User exists : .loadUserByUsername(email) should return the expected User")
    public void loadUserByUsername() {

        when(userRepository.findByEmail("email@domain.com")).thenReturn(Optional.ofNullable(user1));

        UserDetails collectedUser = userService.loadUserByUsername("email@domain.com");

        Assertions.assertThat(collectedUser).isNotNull();
        Assertions.assertThat(collectedUser.getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(collectedUser.getUsername()).isEqualTo(user1.getEmail());
    }

    @Test
    @DisplayName("User doesn't exist : .loadUserByUsername(email) should throw an error with the expected message")
    public void loadMissingUserByUsername() {

        when(userRepository.findByEmail("email@domain.com")).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("email@domain.com");
        });
        
        Assertions.assertThat(exception.getMessage()).isEqualTo("User not valid.");
    }

    @Test
    @DisplayName("Save User")
    public void saveUser() {

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user1);

        User collectedUser = userService.saveUser(user1);

        Assertions.assertThat(collectedUser).isNotNull();
        Assertions.assertThat(collectedUser.getFirstname()).isEqualTo(user1.getFirstname());
        Assertions.assertThat(collectedUser.getLastname()).isEqualTo(user1.getLastname());
        Assertions.assertThat(collectedUser.getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(collectedUser.getUsername()).isEqualTo(user1.getEmail());
    }

}
