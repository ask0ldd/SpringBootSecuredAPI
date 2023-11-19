package com.example.restapi2;

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
import com.example.restapi2.services.UserService;

@EnableConfigurationProperties(RsaKeyProperties.class) // !!!
@SpringBootApplication
public class Restapi2Application implements CommandLineRunner {

	@Autowired
	private UserService userService;

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

		userService.saveUser(new User("Laurent", "GINA", "laurentgina@mail.com",
				passwordEncoder.encode("laurent"), Role.ADMIN));
		userService.saveUser(new User("Sophie", "FONCEK", "sophiefoncek@mail.com",
				passwordEncoder.encode("sophie"), Role.USER));
		userService.saveUser(new User("Agathe", "FEELING", "agathefeeling@mail.com",
				passwordEncoder.encode("agathe"), Role.USER));

		/*
		 * Iterable<User> users = userService.getUsers();
		 * System.out.println(users);
		 */

	}

}