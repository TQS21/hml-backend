package com.service.hml;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.service.hml.entities.*;
import com.service.hml.repositories.HistoryRepository;
import com.service.hml.repositories.HmlRepository;
import com.service.hml.repositories.UserRepository;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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

     private final WebClient apiClient = WebClient.create("http://localhost:9090");

//    private final WebClient apiClient = WebClient.builder()
//            .clientConnector(new ReactorClientHttpConnector(
//                            HttpClient.create()
//                                    .resolver(DefaultAddressResolverGroup.INSTANCE)
//                    ))
//            .baseUrl("http://localhost:9090")
//            .build();


    public ResponseEntity<User> makeDelivery(OrderDTO orderDTO){
        Gson gson = new Gson();

        Order order = orderDTO.createOrder();

        String external_response = apiClient.post()
                .uri("/delivery/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), JsonObject.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonObject jsonResp = gson.fromJson(external_response, JsonObject.class);
        JsonPrimitive orderId = jsonResp.getAsJsonPrimitive("id");
        int id = gson.fromJson(orderId, int.class);

        User user = userRepository.findByEmail(orderDTO.getUserDTO().getEmail());
        user.setDelivery(id);

        return ResponseEntity.status(HttpStatus.OK)
                .header("login-in")
                .body(user);
    }

    public ResponseEntity<String> checkDelivery(UserDTO userDTO){
        User user = userRepository.findByEmail(userDTO.getEmail());

        String external_response = apiClient.get()
                .uri("/delivery/"+user.getDelivery())
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().create();

        System.out.println(external_response);

        JsonObject full_json_response = gson.fromJson(external_response, JsonObject.class);
//
//        JsonArray response = full_json_response.getAsJsonArray("status");
//
//        String allInfo = gson.toJson(response.get(response.size()-1).getAsJsonObject());
//
//        List<Status> allStatus = (List<Status>) gson.fromJson(allInfo, Status.class);
//
//        System.out.println(allStatus);
        return null;
    }

    public ResponseEntity<Set<History>> getHistory(UserDTO userDTO){

        User user = userRepository.findByEmail(userDTO.getEmail());

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("find-all-books")
                .body(user.getHistory());

        //return hmlRepository.findAll();
    }

    public ResponseEntity<List<Book>> getAllBooks(){

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("find-all-books")
                .body(hmlRepository.findAll());

        //return hmlRepository.findAll();
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

        // return new ResponseEntity<Book>(book, status);
    }

    public ResponseEntity<Book> addNewBook(BookDTO book){
        Book saved = hmlRepository.save(book.toBookEntity());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("new-book-added")
                .body(book.toBookEntity());

        //return new ResponseEntity<Book>(saved, HttpStatus.CREATED);
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

        //return new ResponseEntity<User>(user, status);
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

        //return new ResponseEntity<User>(user, status);
    }

}
