package com.service.hml;

import com.service.hml.entities.*;
import com.service.hml.repositories.HmlRepository;
import com.service.hml.repositories.OrderRepository;
import com.service.hml.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class HmlServiceTests {

    @Mock( lenient = true)
    private HmlRepository hmlRepository;

    @Mock( lenient = true)
    private UserRepository userRepository;

    @Mock( lenient = true)
    private OrderRepository orderRepository;

    @InjectMocks
    private HmlService hmlService = new HmlService();

    @Test
    void whenValidInput_thenCreateBook(){
        BookDTO ford = new BookDTO(10,"Ford", "2014 Tauros", "", 10.0);

        ResponseEntity<Book> found = hmlService.addNewBook(ford);
        assertThat(found.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void whenGetUserHistory_thenGetHistory(){
        User user = new User("test", "test@gmail.com", "1234",123456789);
        Book ford = new Book("Ford", "2014 Tauros", "", 10.0);
        Book audi = new Book("Audi", "Audi A8", "", 5.0);

        Set<History> history = new HashSet<>();
        History history1 = new History(user,ford);
        History history2 = new History(user,audi);
        user.setHistory(history);

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        assertThat(history).isEqualTo(hmlService.getHistory(UserDTO.fromUserEntity(user)).getBody());
    }

    @Test
    void whenGetBookAllBooks_thenGetAllBooks(){
        Book ford = new Book("Ford", "2014 Tauros", "", 10.0);
        Book audi = new Book("Audi", "Audi A8", "", 5.0);
        Book bmw = new Book("BMW", "BMW M4", "", 1.0);

        List<Book> addedBooks = Arrays.asList(ford,audi,bmw);

        Mockito.when(hmlRepository.findAll()).thenReturn(addedBooks);

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
        UserDTO user = new UserDTO("test", "test@gmail.com", "1234", 921593214);
        Address address = new Address("PT","841","Ovar","rua santo andre");
        Book ford = new Book("Ford", "2014 Tauros", "", 10.0);
        List<OrderStatsDTO> orderedBooks = new ArrayList<>();
        orderedBooks.add(new OrderStatsDTO(ford,2));

        OrderDTO orderDTO = new OrderDTO(user,address,orderedBooks);

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user.toUserEntity(user));

        assertThat(userRepository.findByEmail(user.getEmail()).getDelivery()).isEqualTo(-1);

        ResponseEntity<User> result = hmlService.makeDelivery(orderDTO);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getDelivery()).isGreaterThan(-1);
    }

    @Test
    void whenCheckDeliveryStatus_returnDeliveryStatus(){
        ResponseEntity<String> result = hmlService.checkDelivery(1);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(result.getBody()).isEqualTo("QUEUED");
    }

    @Test
    void whenCheckOrder_returnAllOrders(){
        Book ford = new Book("Ford", "2014 Tauros", "", 10.0);
        Book audi = new Book("Audi", "Audi A8", "", 5.0);

        OrderStats orderStats1 = new OrderStats(ford,1,10);
        OrderStats orderStats2 = new OrderStats(audi,1,5);

        List<OrderStats> orderStats = new ArrayList<>();
        orderStats.add(orderStats1);
        orderStats.add(orderStats2);

        Mockito.when(orderRepository.findByOrderId(orderStats1.getOrderId())).thenReturn(orderStats);

        ResponseEntity<List<OrderStats>> response = hmlService.checkOrder(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).isEqualTo(orderStats);
    }

    @Test
    void whenLogginCorrectly_loggin() throws Exception {
        User user = new User("test1", "test1@gmail.com","1234",123456789);
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
        User user = new User("test1", "test1@gmail.com","1234",123456789);
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
        User user = new User("test1", "test1@gmail.com","1234",123456789);
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
        User user = new User("test1", "test1@gmail.com","1234",123456789);

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        ResponseEntity<User> result = hmlService.register(UserDTO.fromUserEntity(user));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertThat(result.getBody().getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getBody().getName()).isEqualTo(user.getName());
    }
}
