package com.service.hml;

import com.service.hml.entities.*;
import com.service.hml.repositories.HistoryRepository;
import com.service.hml.repositories.HmlRepository;
import com.service.hml.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = HmlApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class HmlRestApiTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private HmlRepository hmlRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistoryRepository historyRepository;

    Book ford = new Book("Ford", "2014 Tauros", "", 10.0);
    User test1 = new User("test1", "test1@gmail.com","1234",123456789);

    @BeforeEach
    public void insertIntoDb() {
        historyRepository.deleteAll();
        hmlRepository.deleteAll();
        userRepository.deleteAll();

        hmlRepository.saveAndFlush(ford);
        userRepository.saveAndFlush(test1);
    }

    @Test
    void whenValidInput_thenCreateBook() throws IOException, Exception{
        mvc.perform(post("/hml/api/addBook")
                        .content(asJsonString(ford))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());

        List<Book> found = hmlRepository.findAll();
        assertThat(found).extracting(Book::getTitle).containsOnly("Ford");
    }

    @Test
    void whenGetBookAllBooks_thenGetAllBooks() throws Exception {
        Book audi = new Book("Audi", "Audi A8", "", 5.0);
        Book bmw = new Book("BMW", "BMW M4", "", 1.0);

        hmlRepository.saveAndFlush(audi);
        hmlRepository.saveAndFlush(bmw);

        mvc.perform(get("/hml/api/allBooks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void whenValidTittle_thenGetBook() throws IOException, Exception{
        mvc.perform(get("/hml/api/book/"+ford.getTitle()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
    }

    @Test
    void whenInvalidTittle_thenGetNotFound() throws IOException, Exception{
        mvc.perform(get("/hml/api/book/title").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenValidInput_thenCreateDelivery() throws IOException, Exception{
        UserDTO user = new UserDTO("test", "test@gmail.com", "1234", 921593214);
        userRepository.saveAndFlush(user.toUserEntity(user));
        Address address = new Address("PT","841","Ovar","rua santo andre");
        OrderDTO order = new OrderDTO(user,address);
        mvc.perform(post("/hml/api/delivery")
                        .content(asJsonString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenCheckUserOrder_returnUserOrder() throws IOException, Exception{
        mvc.perform(get("/hml/api/delivery/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound());
    }

    @Test
    void whenCheckOrderById_returnOrdersById() throws IOException, Exception{
        mvc.perform(get("/hml/api/order/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound());
    }

    @Test
    void whenLogginCorrectly_loggin() throws Exception {
        mvc.perform(post("/hml/api/login/")
                .content(asJsonString(test1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenLogginIncorrectly_failTologgin() throws Exception{
        test1.setPassword("4321");
        mvc.perform(post("/hml/api/login/")
                        .content(asJsonString(test1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void whenUserNotFound_returnsNotFound() throws Exception{
        User test2 = new User("test2", "test2@gmail.com","1234",123456789);

        mvc.perform(post("/hml/api/login/")
                        .content(asJsonString(test2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenRegisterNewUser_returnsAcceptable() throws Exception{
        User test1 = new User("test2", "test2@gmail.com","1234",123456789);

        mvc.perform(post("/hml/api/register/")
                        .content(asJsonString(test1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void whenRegisterOldUser_returnsNotAcceptable() throws Exception{
        User test1 = new User("test1", "test1@gmail.com","1234",123456789);

        mvc.perform(post("/hml/api/register/")
                        .content(asJsonString(test1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void whenGetUserHistory_thenGetHistory() throws Exception {
        History history = new History(test1, ford);
        historyRepository.saveAndFlush(history);
        System.out.println(historyRepository.findAll());

        mvc.perform(get("/hml/api/history")
                        .content(asJsonString(UserDTO.fromUserEntity(test1)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].book.title", is(ford.getTitle())));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
