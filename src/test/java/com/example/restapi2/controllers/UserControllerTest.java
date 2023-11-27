package com.example.restapi2.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.restapi2.Restapi2Application;
import com.example.restapi2.configuration.RSAKeyProperties;
import com.example.restapi2.configuration.SecurityConfiguration;
import com.example.restapi2.models.Role;
import com.example.restapi2.models.User;
import com.example.restapi2.repositories.RoleRepository;
import com.example.restapi2.services.UserService;
import com.example.restapi2.services.ValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

/*@SpringBootTest(classes = { com.example.restapi2.Restapi2Application.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)*/
//@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { /* com.example.restapi2.Restapi2Application.class, */
                com.example.restapi2.configuration.SecurityConfiguration.class,
                com.example.restapi2.configuration.RSAKeyProperties.class })
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false) // bypass spring security
@ExtendWith(MockitoExtension.class)
// @SpringBootTest(classes = { com.example.restapi2.Restapi2Application.class })
// @AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
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
        private final String laurentPassword = passwordEncoder.encode("laurent");

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
                given(userService.getUsers()).willAnswer((invocation -> (Iterable<User>) userCollection));

                ResultActions response = mockMvc.perform(get("/users"));

                response.andExpect(MockMvcResultMatchers.status().isOk());
        }
}
