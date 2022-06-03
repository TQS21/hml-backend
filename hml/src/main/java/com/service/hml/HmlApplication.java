package com.service.hml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HmlApplication {

	private static final Logger log = LoggerFactory.getLogger(HmlApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(HmlApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(HmlRepository repository) {
		return (args) -> {

			repository.save(new Book(1,"The Godfather", "Mario Puzo",""));
			repository.save(new Book(2,"Boku no Hero", "Kōhei Horikoshi",""));
			repository.save(new Book(3,"Spy X Family", "Tatsuya Endo",""));
			repository.save(new Book(4,"Chainsaw Man", "Tatsuki Fujimoto",""));
			repository.save(new Book(5,"The Hobbit", "J. R. R. Tolkien",""));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Book book : repository.findAll()) {
				log.info(book.toString());
			}

			// fetch an individual book by tittle
			Book book = repository.findByTitle("The Godfather");
			log.info("Customer found with findById(1L):");
			log.info("--------------------------------");
			log.info(book.toString());
			log.info("");
		};
	}

}
