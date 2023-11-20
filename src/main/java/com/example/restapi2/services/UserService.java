package com.example.restapi2.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// import com.example.restapi2.models.Role;
import com.example.restapi2.models.User;
import com.example.restapi2.repositories.UserRepository;

import lombok.Data;

@Data
@Service
/*
 * @Service : tout comme l’annotation @Repository, c’est une spécialisation
 * de @Component. Son rôle est donc le même, mais son nom a une valeur
 * sémantique pour ceux qui lisent votre code.
 */
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUser(final Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // return userRepository.findByEmail(username);
        System.out.println("\n\n***************loadUserByUsername***************\n\n");

        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not valid."));
        /*
         * Optional<User> user = userRepository.findByEmail(username)
         * if (user.isPresent())
         * return new User("Agathe", "FEELING", "agathefeeling@mail.com",
         * "agathe", Role.USER);
         */

        // throw new UsernameNotFoundException("User not Found.");
    }

}
