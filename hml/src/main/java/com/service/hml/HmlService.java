package com.service.hml;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.service.hml.entities.Address;
import com.service.hml.entities.Book;
import com.service.hml.entities.BookDTO;
import com.service.hml.entities.User;
import com.service.hml.entities.UserDTO;
import com.service.hml.repositories.HmlRepository;
import com.service.hml.repositories.UserRepository;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

@Service
public class HmlService {

    Logger logger = LoggerFactory.getLogger(HmlService.class);

    @Autowired
    private HmlRepository hmlRepository;

    @Autowired
    private UserRepository userRepository;

    // private final WebClient apiClient = WebClient.create("http://deti-tqs-05:8080");

    private final WebClient apiClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(
                            HttpClient.create()
                                    .resolver(DefaultAddressResolverGroup.INSTANCE)
                    ))
            .baseUrl("http://deti-tqs-05:8080")
            .build();


    public String makeDelivery(BookDTO bookDTO, Address add, UserDTO user, int phone){
        //hmlRepository.findByTitle(bookDTO.toBookEntity().getTitle()).setAvailable(false);

        Address address = add;

        HashMap<String, Object> body = new HashMap<>();
        HashMap<String, String> client = new HashMap<>();

        body.put("shop_id", 0);
        body.put("courier_id", 0);
        client.put("name", user.getName());
        client.put("phone_number",String.valueOf(phone));
        body.put("client", client);
        body.put("address", address);

        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String gsonString = gson.toJson(body,gsonType);

    /*String external_response = apiClient.post()
            .uri("/delivery")
            .acceptCharset(StandardCharsets.UTF_8)
            .body(Mono.just(gsonString), String.class)
            .retrieve()
            .bodyToMono(String.class)
            .block();

        JsonObject jsonResp = gson.fromJson(external_response, JsonObject.class);
        JsonArray getDeliveryResponse = jsonResp.getAsJsonArray("status");
        return external_response;*/
        return gsonString;
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
