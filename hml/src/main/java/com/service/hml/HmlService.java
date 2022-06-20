package com.service.hml;

import com.google.gson.*;
import com.service.hml.entities.*;
import com.service.hml.repositories.HistoryRepository;
import com.service.hml.repositories.HmlRepository;
import com.service.hml.repositories.OrderRepository;
import com.service.hml.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class HmlService {

    Logger logger = LoggerFactory.getLogger(HmlService.class);

    @Autowired
    private HmlRepository hmlRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private OrderRepository orderRepository;

     private final WebClient apiClient = WebClient.create("http://deti-tqs-05:9090/");

    public ResponseEntity<User> makeDelivery(OrderDTO orderDTO){
        Order order = orderDTO.createOrder();

        String external_response = apiClient.post()
                .uri("delivery/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), JsonObject.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Gson gson = new Gson();
        JsonObject jsonResp = gson.fromJson(external_response, JsonObject.class);
        JsonPrimitive orderId = jsonResp.getAsJsonPrimitive("id");
        int id = gson.fromJson(orderId, int.class);

        User user = userRepository.findByEmail(orderDTO.getUserDTO().getEmail());
        user.setDelivery(id);

        return ResponseEntity.status(HttpStatus.OK)
                .header("login-in")
                .body(user);
    }

    public ResponseEntity<String> checkDelivery(int orderId){

        String external_response = apiClient.get()
                .uri("delivery/"+orderId)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().create();
        System.out.println(external_response);
        JsonObject response = gson.fromJson(external_response, JsonObject.class);
        JsonObject orderStatus = response.getAsJsonObject("orderStatus");
        JsonPrimitive status = orderStatus.getAsJsonPrimitive("status");

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("find-all-books")
                .body(status.getAsString());
    }

    public ResponseEntity<List<OrderStats>> checkOrder(int orderId){

        List<OrderStats> order = orderRepository.findByOrderId(orderId);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("find-all-books")
                .body(order);
    }

    public ResponseEntity<Set<History>> getHistory(UserDTO userDTO){

        User user = userRepository.findByEmail(userDTO.getEmail());

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("find-all-books")
                .body(user.getHistory());
    }

    public ResponseEntity<List<Book>> getAllBooks(){

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("find-all-books")
                .body(hmlRepository.findAll());
    }

    public ResponseEntity<Book> getBookDetails(String title){
        Book book = hmlRepository.findByTitle(title);
        HttpStatus status;

        if(book != null){
            status = HttpStatus.FOUND;
        }
        else {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status)
                .header("find-book-"+title,"book")
                .body(book);
    }

    public ResponseEntity<Book> addNewBook(BookDTO book){
        Book saved = hmlRepository.save(book.toBookEntity());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("new-book-added")
                .body(book.toBookEntity());
    }

    public ResponseEntity<User> login(UserDTO userDTO){
        User user = userRepository.findByEmail(userDTO.getEmail());
        HttpStatus status;

        if(user != null){
            if(user.getPassword().equals(userDTO.getPassword()))
                status = HttpStatus.OK;
            else status = HttpStatus.NOT_ACCEPTABLE;
        }
        else status = HttpStatus.NOT_FOUND;

        return ResponseEntity.status(status)
                .header("login-in")
                .body(user);
    }

    public ResponseEntity<User> register(UserDTO userDTO){
        User user = userDTO.toUserEntity(userDTO);
        HttpStatus status;

        if(userRepository.findByEmail(userDTO.getEmail()) == null){
            status = HttpStatus.ACCEPTED;
            userRepository.save(user);
        }
        else status = HttpStatus.NOT_ACCEPTABLE;

        return ResponseEntity.status(status)
                .header("registered")
                .body(user);
    }

}
