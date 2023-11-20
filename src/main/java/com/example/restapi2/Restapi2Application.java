package com.example.restapi2;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

		// BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		/*
		 * userService.saveUser(new User("Laurent", "GINA", "laurentgina@mail.com",
		 * passwordEncoder.encode("laurent")));
		 * userService.saveUser(new User("Sophie", "FONCEK", "sophiefoncek@mail.com",
		 * passwordEncoder.encode("sophie")));
		 * userService.saveUser(new User("Agathe", "FEELING", "agathefeeling@mail.com",
		 * passwordEncoder.encode("agathe")));
		 */

		Role userRole = roleRepository.findByAuthority("USER").get();
		Set<Role> userAuthority = new HashSet<>();
		userAuthority.add(userRole);

		Role adminRole = roleRepository.findByAuthority("ADMIN").get();
		Set<Role> adminAuthority = new HashSet<>();
		adminAuthority.add(userRole);

		userService.saveUser(new User("Laurent", "GINA", "laurentgina@mail.com",
				passwordEncoder.encode("laurent"), adminAuthority));
		userService.saveUser(new User("Sophie", "FONCEK", "sophiefoncek@mail.com",
				passwordEncoder.encode("sophie"), userAuthority));
		userService.saveUser(new User("Agathe", "FEELING", "agathefeeling@mail.com",
				passwordEncoder.encode("agathe"), userAuthority));

		/*
		 * Iterable<User> users = userService.getUsers();
		 * System.out.println(users);
		 */

	}

}