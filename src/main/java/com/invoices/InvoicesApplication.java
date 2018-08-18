package com.invoices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * An application to generate, delete, update and read invoices for MeritKaptial
 * @author psoutzis, email: soutzis.petros@gmail.com
 * @version 1.0
 */
@SpringBootApplication
public class InvoicesApplication {

	public static void main(String[] args) {

		SpringApplication.run(InvoicesApplication.class, args);
	}
}
