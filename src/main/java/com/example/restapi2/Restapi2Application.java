package com.example.restapi2;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.restapi2.configuration.RsaKeyProperties;
import com.example.restapi2.models.Role;
import com.example.restapi2.models.User;
import com.example.restapi2.repositories.RoleRepository;
import com.example.restapi2.services.UserService;

@EnableConfigurationProperties(RsaKeyProperties.class) // !!!
@SpringBootApplication
public class Restapi2Application implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(Restapi2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// init role table
		if (roleRepository.findByAuthority("ADMIN").isPresent())
			return;
		Role adminRole = roleRepository.save(new Role("ADMIN"));
		roleRepository.save(new Role("USER"));

		Role userRole = roleRepository.findByAuthority("USER").get();
		Set<Role> userAuthority = new HashSet<>();
		userAuthority.add(userRole);

		System.out.println("\n\n***************" + userRole.getAuthority() + "***************\n\n");

		Set<Role> adminAuthority = new HashSet<>();
		adminAuthority.add(adminRole);

		System.out.println("\n\n***************" + adminRole.getAuthority() + "***************\n\n");

		userService.saveUser(new User(null, "Laurent", "GINA", "laurentgina@mail.com",
				passwordEncoder.encode("laurent"), adminAuthority));
		userService.saveUser(new User(null, "Sophie", "FONCEK", "sophiefoncek@mail.com",
				passwordEncoder.encode("sophie"), userAuthority));
		userService.saveUser(new User(null, "Agathe", "FEELING", "agathefeeling@mail.com",
				passwordEncoder.encode("agathe"), userAuthority));

		/*
		 * Iterable<User> users = userService.getUsers();
		 * System.out.println(users);
		 */

	}

}