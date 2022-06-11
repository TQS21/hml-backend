package com.service.hml;

import com.service.hml.entities.Address;
import com.service.hml.entities.Book;
import com.service.hml.entities.BookDTO;
import com.service.hml.entities.User;
import com.service.hml.entities.UserDTO;
import com.service.hml.repositories.HmlRepository;
import com.service.hml.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class HmlServiceTests {

    @Mock( lenient = true)
    private HmlRepository hmlRepository;

    @Mock( lenient = true)
    private UserRepository userRepository;

    @InjectMocks
    private HmlService hmlService = new HmlService();

    @Test
    void whenValidInput_thenCreateBook(){
        BookDTO ford = new BookDTO(10,"Ford", "2014 Tauros", "", 10.0);

        ResponseEntity<Book> found = hmlService.addNewBook(ford);
        assertThat(found.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void whenGetBookAllBooks_thenGetAllBooks(){
        Book ford = new Book("Ford", "2014 Tauros", "", 10.0);
        Book audi = new Book("Audi", "Audi A8", "", 5.0);
        Book bmw = new Book("BMW", "BMW M4", "", 1.0);

        List<Book> addedBooks = Arrays.asList(ford,audi,bmw);

        Mockito.when(hmlRepository.findAll()).thenReturn(addedBooks);
        Mockito.when(hmlService.getAllBooks().getBody()).thenReturn(addedBooks);

        assertThat(addedBooks).isEqualTo(hmlRepository.findAll());
        assertThat(addedBooks).isEqualTo(hmlService.getAllBooks().getBody());
    }

    @Test
    void whenValidTittle_thenGetBook(){
        Book ford = new Book("Ford", "2014 Tauros", "", 10.0);
        String title = "Ford";

        Mockito.when(hmlRepository.findByTitle(title)).thenReturn(ford);

        Book repResult = hmlRepository.findByTitle(title);
        ResponseEntity<Book> result = hmlService.getBookDetails(title);

        assertThat(repResult).isEqualTo(ford);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(result.getBody().getTitle()).isEqualTo(ford.getTitle());
    }

    @Test
    void whenInvalidTittle_thenGetNotFound(){
        String title = "Ford";
        Mockito.when(hmlRepository.findByTitle(title)).thenReturn(null);

        Book repResult = hmlRepository.findByTitle(title);
        ResponseEntity<Book> result = hmlService.getBookDetails(title);

        assertThat(repResult).isEqualTo(null);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(null);
    }

    @Test
    void whenValidInput_thenCreateDelivery(){
        Book ford = new Book("Ford", "2014 Tauros", "", 10.0);
        hmlRepository.saveAndFlush(ford);
        BookDTO book = BookDTO.fromBookEntity(ford);
        UserDTO user = UserDTO.fromUserEntity(new User("test1", "test1@gmail.com","1234"));
        Address address = new Address("Portugal","4505-519","rua fodasse");
        System.out.println(hmlService.makeDelivery(book,address,user,967827582));
    }

    @Test
    void whenLogginCorrectly_loggin() throws Exception {
        User user = new User("test1", "test1@gmail.com","1234");
        String email = "test1@gmail.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        User userResult = userRepository.findByEmail(email);
        ResponseEntity<User> result = hmlService.login(UserDTO.fromUserEntity(user));

        assertThat(userResult).isEqualTo(user);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(user);
    }

    @Test
    void whenLoginIncorrectly_failToLogin() throws Exception{
        User user = new User("test1", "test1@gmail.com","1234");
        UserDTO userDTO = new UserDTO("test1", "test1@gmail.com","4321");
        String email = "test1@gmail.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        User userResult = userRepository.findByEmail(email);
        ResponseEntity<User> result = hmlService.login(userDTO);

        assertThat(userResult).isEqualTo(user);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertThat(result.getBody()).isEqualTo(user);
    }

    @Test
    void whenUserNotFound_returnsNotFound() throws Exception{
        User user = new User("test1", "test1@gmail.com","1234");
        String email = "test1@gmail.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);

        User userResult = userRepository.findByEmail(email);
        ResponseEntity<User> result = hmlService.login(UserDTO.fromUserEntity(user));

        assertThat(userResult).isEqualTo(null);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(null);
    }

    @Test
    void whenRegisterNewUser_returnsAccepted() throws Exception{
        UserDTO user = new UserDTO("test1", "test1@gmail.com","1234");

        ResponseEntity<User> result = hmlService.register(user);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(result.getBody().getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getBody().getName()).isEqualTo(user.getName());
    }

    @Test
    void whenRegisterExistingUser_returnsNotAccepted() throws Exception{
        User user = new User("test1", "test1@gmail.com","1234");

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        ResponseEntity<User> result = hmlService.register(UserDTO.fromUserEntity(user));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertThat(result.getBody().getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getBody().getName()).isEqualTo(user.getName());
    }
}
