package com.service.hml;

import com.service.hml.entities.*;
import com.service.hml.repositories.HmlRepository;
import com.service.hml.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/hml/api")
public class HmlAPI {

    Logger logger = LoggerFactory.getLogger(HmlAPI.class);

    @Autowired
    private HmlService hmlService;

    @PostMapping("/delivery")
    public void makeDelivery(@RequestBody @NotNull BookDTO bookDTO,
                             @RequestBody @NotNull Address address,
                             @RequestBody @NotNull UserDTO user,
                             @RequestBody @NotNull int phone){
        hmlService.makeDelivery(bookDTO, address, user, phone);
    }

    @GetMapping("/allBooks")
    public List<Book> getAllBooks(){
        return hmlService.getAllBooks().getBody();
    }

    @GetMapping("/book/{title}")
    public ResponseEntity<Book> getBookDetails(@PathVariable(required = true, name = "title") String title){
        return hmlService.getBookDetails(title);
    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> addNewBook(@RequestBody @NotNull BookDTO book){
        return hmlService.addNewBook(book);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @NotNull UserDTO userDTO){
        return hmlService.login(userDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @NotNull UserDTO userDTO){
        return hmlService.register(userDTO);
    }

}
