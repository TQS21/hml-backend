package com.service.hml;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class HmlRespositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HmlRepository hmlRepository;

    @Test
    void whenFindAll_resturnAll(){
        Book ford = new Book(1,"Ford", "2014 Tauros","");
        Book audi = new Book(2,"Audi", "Audi A8","");
        Book bmw = new Book(3,"BMW", "BMW M4","");

        entityManager.persist(ford);
        entityManager.persist(bmw);
        entityManager.persist(audi);
        entityManager.flush();

        List<Book> allCars = hmlRepository.findAll();

        assertThat(allCars).hasSize(3).extracting(Book::getTitle).containsOnly(ford.getTitle(), audi.getTitle(), bmw.getTitle());
    }

    @Test
    void whenFindByExistingTittle_findBook(){
        Book ford = new Book(1,"Ford", "2014 Tauros","");
        entityManager.persistAndFlush(ford);

        Book found = hmlRepository.findByTitle(ford.getTitle());
        assertThat( found ).isEqualTo(ford);
    }

    @Test
    void whenFindByInvalidTittle_returnNull(){
        Book found = hmlRepository.findByTitle("Ford");
        assertThat( found ).isNull();
    }
}
