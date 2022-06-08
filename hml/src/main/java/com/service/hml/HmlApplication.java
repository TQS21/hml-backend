package com.service.hml;

import com.service.hml.entities.Book;
import com.service.hml.entities.User;
import com.service.hml.repositories.HmlRepository;
import com.service.hml.repositories.UserRepository;
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
	public CommandLineRunner demo(HmlRepository hmlRepository, UserRepository userRepository) {
		return (args) -> {

			hmlRepository.save(new Book("The Godfather", "Mario Puzo","https://images-na.ssl-images-amazon.com/images/I/81IHPwG1tbL.jpg", 15.0));
			hmlRepository.save(new Book("Boku no Hero Volume 1", "Kōhei Horikoshi","https://static.fnac-static.com/multimedia/Images/PT/NR/af/c0/48/4767919/1507-1.jpg", 8.5));
			hmlRepository.save(new Book("Spy X Family Volume 1", "Tatsuya Endo","https://kbimages1-a.akamaihd.net/07d1ab28-6fb4-4bfd-b125-21354e434b17/1200/1200/False/spy-x-family-vol-1.jpg", 8.0));
			hmlRepository.save(new Book("Chainsaw Man Volume 1", "Tatsuki Fujimoto","https://static.wikia.nocookie.net/chainsaw-man/images/c/c0/ChainsawManVolume1.jpeg/revision/latest?cb=20210921233305&path-prefix=pt-br", 8.0));
			hmlRepository.save(new Book("The Hobbit", "J. R. R. Tolkien","https://images-na.ssl-images-amazon.com/images/I/710+HcoP38L.jpg", 20.0));

			userRepository.save(new User("test1", "test1@gmail.com","1234"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Book book : hmlRepository.findAll()) {
				log.info(book.toString());
			}

			// fetch an individual book by tittle
			Book book = hmlRepository.findByTitle("The Godfather");
			log.info("Book found with findByTitle(\"The Godfather\"):");
			log.info("--------------------------------");
			log.info(book.toString());
			log.info("");

			// fetch an individual user by email
			User user = userRepository.findByEmail("test1@gmail.com");
			log.info("User found with findByEmail(\"test1@gmail.com\"):");
			log.info("--------------------------------");
			log.info(user.toString());
			log.info("");
		};
	}

}
