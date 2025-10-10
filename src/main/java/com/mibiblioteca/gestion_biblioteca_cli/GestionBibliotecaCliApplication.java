package com.mibiblioteca.gestion_biblioteca_cli;

import com.mibiblioteca.gestion_biblioteca_cli.controller.MenuPrincipalCLI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GestionBibliotecaCliApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionBibliotecaCliApplication.class, args);
	}

}
