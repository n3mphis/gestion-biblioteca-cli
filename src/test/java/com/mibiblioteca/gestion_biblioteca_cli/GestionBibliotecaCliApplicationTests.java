package com.mibiblioteca.gestion_biblioteca_cli;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {"spring.main.web-application-type=none"})
@ActiveProfiles("test")
class GestionBibliotecaCliApplicationTests {

	@Test
	void contextLoads() {
	}

}
