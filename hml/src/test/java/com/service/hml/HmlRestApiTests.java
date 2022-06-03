package com.service.hml;

import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = HmlApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class HmlRestApiTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private HmlRepository repository;

    @BeforeEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    void whenValidInput_thenCreateBook() throws IOException, Exception{
        Book ford = new Book(1, "Ford", "2014 Tauros", "");

        mvc.perform(post("/hml/api/addBook")
                        .content(asJsonString(ford))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());

        List<Book> found = repository.findAll();
        assertThat(found).extracting(Book::getTitle).containsOnly("Ford");
    }

    @Test
    void whenGetBookAllBooks_thenGetAllBooks() throws Exception {
        Book ford = new Book(1, "Ford", "2014 Tauros", "");
        Book audi = new Book(2, "Audi", "Audi A8", "");
        Book bmw = new Book(3, "BMW", "BMW M4", "");

        repository.saveAndFlush(ford);
        repository.saveAndFlush(audi);
        repository.saveAndFlush(bmw);

        mvc.perform(get("/hml/api/allBooks").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void whenValidTittle_thenGetBook() throws IOException, Exception{
        Book ford = new Book(1, "Book", "2014 Tauros", "");
        repository.saveAndFlush(ford);

        System.out.println(repository.findAll());
        mvc.perform(get("/hml/api/book/"+ford.getTitle()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound());
    }

    @Test
    void whenInvalidTittle_thenGetNotFound() throws IOException, Exception{
        mvc.perform(get("/hml/api/book/title").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenValidInput_thenCreateDelivery() throws IOException, Exception{
        Book ford = new Book(1, "Ford", "2014 Tauros", "");
        //mvc.perform(post("/hml/api/delivery").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(ford)));

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
