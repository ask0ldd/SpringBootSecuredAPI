package com.example.restapi2.controllers;

import java.util.Date;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;

import com.example.restapi2.configuration.RsaKeyProperties;
import com.example.restapi2.configuration.SecurityConfiguration;
import com.example.restapi2.models.Role;
import com.example.restapi2.models.User;
import com.example.restapi2.repositories.RoleRepository;
import com.example.restapi2.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

/*@SpringBootTest(classes = { com.example.restapi2.Restapi2Application.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)*/
@RunWith(SpringRunner.class)
/*
 * @AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
 * 
 * @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
 */
// @ComponentScan(basePackages = { "com.example.restapi2" })
@ContextConfiguration(classes = { SecurityConfiguration.class, RsaKeyProperties.class })
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false) // bypass spring security
@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application.properties")
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RsaKeyProperties rsaKeyProperties;

    private Set<Role> roleSet;
    private User user1;
    private User user2;

    @BeforeEach
    public void init() {
        roleSet = Set.of(new Role(1, "ADMIN"));
        user1 = new User(4L, "firstname1", "lastname1", "email1@domain.com", "randomPassword1",
                roleSet, new Date(), new Date());
        user2 = new User(5L, "firstname2", "lastname2", "email2@domain.com", "randomPassword2",
                roleSet, new Date(), new Date());
    }

    @DisplayName("Create some User.")
    @Test
    public void CreateUser() throws Exception {
        given(userService.saveUser(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(user1.getEmail())));
    }
}
