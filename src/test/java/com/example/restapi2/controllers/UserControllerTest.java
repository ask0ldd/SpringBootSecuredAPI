package com.example.restapi2.controllers;

import java.util.ArrayList;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.restapi2.models.Role;
import com.example.restapi2.models.User;
import com.example.restapi2.repositories.RoleRepository;
import com.example.restapi2.services.UserService;
import com.example.restapi2.services.ValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@ContextConfiguration(classes = { UserController.class,
                com.example.restapi2.configuration.SecurityConfiguration.class,
                com.example.restapi2.configuration.RSAKeyProperties.class })
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false) // bypass spring security
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

        @MockBean
        private UserService userService;
        @MockBean
        private RoleRepository roleRepository;
        @MockBean
        private ValidationService validationService;
        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @Autowired
        private PasswordEncoder passwordEncoder;

        private User user1;
        private User user2;
        private User user3;

        @BeforeEach
        public void init() {

                Set<Role> roleSet = Set.of(new Role(1, "ADMIN"));
                Set<Role> roleSet2 = Set.of(new Role(2, "USER"));

                user1 = new User(1L, "Laurent", "GINA", "laurentgina@mail.com",
                                passwordEncoder.encode("laurent"), roleSet);
                user2 = new User(2L, "Sophie", "FONCEK", "sophiefoncek@mail.com",
                                passwordEncoder.encode("sophie"), roleSet2);
                user3 = new User(3L, "Agathe", "FEELING", "agathefeeling@mail.com",
                                passwordEncoder.encode("agathe"), roleSet2);
        }

        @DisplayName("Get all Users.")
        @Test
        @WithMockUser(username = "laurentgina@mail.com", password = "laurent", roles = "ADMIN")
        public void GetUsers() throws Exception {

                ArrayList<User> userCollection = new ArrayList<>();
                userCollection.add(user1);
                userCollection.add(user2);
                userCollection.add(user3);
                // needs to mock public UserDetails loadUserByUsername(String username)
                given(userService.loadUserByUsername(Mockito.anyString())).willAnswer((invocation -> user1));
                given(userService.getUsers()).willAnswer((invocation -> (Iterable<User>) userCollection));

                ResultActions response = mockMvc.perform(get("/users2"));

                response.andExpect(MockMvcResultMatchers.status().isOk());

                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstname",
                                                CoreMatchers.is("Laurent")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastname", CoreMatchers.is("GINA")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email",
                                                CoreMatchers.is("laurentgina@mail.com")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].firstname", CoreMatchers.is("Sophie")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].lastname", CoreMatchers.is("FONCEK")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].email",
                                                CoreMatchers.is("sophiefoncek@mail.com")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].firstname", CoreMatchers.is("Agathe")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].lastname", CoreMatchers.is("FEELING")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].email",
                                                CoreMatchers.is("agathefeeling@mail.com")));
        }

        @DisplayName("get /user/{id} : Get Target User.")
        @Test
        @WithMockUser(username = "laurentgina@mail.com", password = "laurent", roles = "ADMIN")
        public void GetUserById_ReturnUser() throws Exception {
                clearInvocations(userService);
                given(userService.getUser(Mockito.anyLong())).willAnswer((invocation -> user1));

                ResultActions response = mockMvc.perform(get("/user/1"));

                verify(userService, times(1)).getUser(Mockito.anyLong());
                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", CoreMatchers.is("Laurent")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", CoreMatchers.is("GINA")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                                                CoreMatchers.is("laurentgina@mail.com")));
        }
}
