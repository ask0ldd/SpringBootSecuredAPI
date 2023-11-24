package com.example.restapi2.services;

import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Test
    @DisplayName("Save() saves one Message into DB.")
    public void getUser() {

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role(1, "ADMIN"));
        User user = new User(1L, "firstname", "lastname", "email@domain.com", "randomPassword",
                roleSet);

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        Optional<User> collectedUser = userService.getUser(1L);

        Assertions.assertThat(collectedUser.get()).isNotNull();
        Assertions.assertThat(collectedUser.get().getUserId()).isGreaterThan(0);
        Assertions.assertThat(collectedUser.get().getFirstname()).isEqualTo("firstname");
        Assertions.assertThat(collectedUser.get().getLastname()).isEqualTo("lastname");
        Assertions.assertThat(collectedUser.get().getPassword()).isEqualTo("randomPassword");
        Assertions.assertThat(collectedUser.get().getEmail()).isEqualTo("email@domain.com");
        for (GrantedAuthority role : collectedUser.get().getAuthorities()) {
            Assertions.assertThat(role.getAuthority()).isEqualTo("ADMIN");
        } // !! to improve

    }
}
