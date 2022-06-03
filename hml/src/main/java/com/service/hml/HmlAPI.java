package com.service.hml;

import com.service.hml.entities.Book;
import com.service.hml.entities.BookDTO;
import com.service.hml.entities.User;
import com.service.hml.entities.UserDTO;
import com.service.hml.repositories.HmlRepository;
import com.service.hml.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hml/api")
public class HmlAPI {

    Logger logger = LoggerFactory.getLogger(HmlAPI.class);

    @Autowired
    private HmlRepository hmlRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/delivery")
    public void makeDelivery(@RequestBody @NotNull BookDTO bookDTO){
        Book book = bookDTO.toBookEntity();

    }

    @GetMapping("/allBooks")
    public List<Book> getAllBooks(){
        return hmlRepository.findAll();
    }

    @GetMapping("/book/{title}")
    public ResponseEntity<Book> getBookDetails(@PathVariable(required = true, name = "title") String title){
        Book book = hmlRepository.findByTitle(title);
        HttpStatus status;

        if(book != null){
            status = HttpStatus.FOUND;
        }
        else {
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<Book>(book, status);
    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> addNewBook(@RequestBody @NotNull BookDTO book){
        Book saved = hmlRepository.save(book.toBookEntity());
        return new ResponseEntity<Book>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loggin(@RequestBody @NotNull UserDTO userDTO){
        User user = userRepository.findByEmail(userDTO.getEmail());
        HttpStatus status;

        if(user != null){
            if(user.getPassword().equals(userDTO.getPassword()))
                status = HttpStatus.ACCEPTED;
            else status = HttpStatus.NOT_ACCEPTABLE;
        }
        else status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<User>(user, status);
    }
}
