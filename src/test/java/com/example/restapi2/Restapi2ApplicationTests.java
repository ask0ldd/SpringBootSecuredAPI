package com.example.restapi2;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Restapi2ApplicationTests.class)
// !!!!! creates a profile so we can ignore specific parts of the main app when
// init for context
@ActiveProfiles("test")
class Restapi2ApplicationTests {

	@Test
	void contextLoads(ApplicationContext context) {
		Assertions.assertThat(context).isNotNull();
	}

}
