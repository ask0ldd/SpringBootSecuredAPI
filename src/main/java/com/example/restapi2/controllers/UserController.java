package com.example.restapi2.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi2.models.User;
import com.example.restapi2.services.UserService;
import com.example.restapi2.services.ValidationService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "Authorization")
public class UserController {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserService userService;

    // @GetMapping(value = "/users", produces = "application/json", consumes =
    // "application/json")
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        try {
            return new ResponseEntity<Iterable<User>>(userService.getUsers(), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find any User.", HttpStatus.NOT_FOUND);
        }
    }

    /*
     * @GetMapping("/users2")
     * public Iterable<User> getUsers2() {
     * return userService.getUsers();
     * }
     */

    @GetMapping("/users2")
    public ResponseEntity<?> getUsers2() {
        try {
            return new ResponseEntity<Iterable<User>>(userService.getUsers(), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find any User.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/test1")
    public ResponseEntity<?> getString() {
        try {
            User currentUser = userService.getUser(1L);
            return new ResponseEntity<User>(currentUser, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested User.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/test2")
    public ResponseEntity<Iterable<String>> getString2() {
        List<String> list = new ArrayList<>();
        list.add("Volvo");
        return new ResponseEntity<Iterable<String>>(list, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") final Long id) {
        try {
            User currentUser = userService.getUser(id);
            return new ResponseEntity<User>(currentUser, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested User.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.saveUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>("Can't create the target User.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") final Long id, @RequestBody User user) {
        try {
            User currentUser = userService.getUser(id); // !!! deal with throw

            String firstName = user.getFirstname();
            if (firstName != null && validationService.isName(firstName)) {
                currentUser.setFirstname(firstName);
            }

            String lastName = user.getLastname();
            if (lastName != null && validationService.isName(lastName)) {
                currentUser.setLastname(lastName);
            }

            String mail = user.getEmail();
            if (mail != null && validationService.isEmail(mail)) {
                currentUser.setEmail(mail);
            }

            User modifiedUser = userService.saveUser(currentUser);

            return new ResponseEntity<User>(/* userService.getUser(id) */ modifiedUser, HttpStatus.OK);
        } catch (Exception exception) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<String>("Can't find the requested User.", headers, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") final Long id) {
        userService.deleteUser(id);
    }

}

/*
 * @PostMapping("/login")
 * public ResponseEntity<?> getUserByEmail(@RequestBody String email) {
 * Optional<User> userOptional = userService.getUserByEmail(email);
 * if (userOptional.isPresent()) {
 * return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
 * } else {
 * return new ResponseEntity<>("Can't find the requested User.",
 * HttpStatus.NOT_FOUND);
 * }
 * }
 */