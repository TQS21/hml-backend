package com.service.hml;

import com.service.hml.entities.Book;
import com.service.hml.entities.OrderStats;
import com.service.hml.entities.User;
import com.service.hml.repositories.HmlRepository;
import com.service.hml.repositories.OrderRepository;
import com.service.hml.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class HmlRespositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HmlRepository hmlRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;
    @BeforeEach
    public void resetDb() {
        hmlRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void whenFindAll_resturnAll(){
        Book ford = new Book("Ford", "2014 Tauros","",10.0);
        Book audi = new Book("Audi", "Audi A8","",5.0);
        Book bmw = new Book("BMW", "BMW M4","",1.0);

        entityManager.persist(ford);
        entityManager.persist(bmw);
        entityManager.persist(audi);
        entityManager.flush();

        List<Book> allCars = hmlRepository.findAll();

        assertThat(allCars).hasSize(3).extracting(Book::getTitle).containsOnly(ford.getTitle(), audi.getTitle(), bmw.getTitle());
    }

    @Test
    void whenFindByExistingTittle_findBook(){
        Book ford = new Book("Ford", "2014 Tauros","",10.0);
        entityManager.persistAndFlush(ford);

        Book found = hmlRepository.findByTitle(ford.getTitle());
        assertThat( found ).isEqualTo(ford);
    }

    @Test
    void whenFindByInvalidTittle_returnNull(){
        Book found = hmlRepository.findByTitle("Ford");
        assertThat( found ).isNull();
    }

    @Test
    void whenFindByValidEmail_findUser(){
        User test1 = new User("test1", "test1@gmail.com","1234",123456789);
        entityManager.persistAndFlush(test1);

        User found = userRepository.findByEmail(test1.getEmail());
        assertThat( found ).isEqualTo(test1);
    }

    @Test
    void whenFindByInvalidEmail_findUser(){
        User found = userRepository.findByEmail("test1");
        assertThat( found ).isNull();
    }

    @Test
    void whenSearchForOrdersByOrderId_findOrders(){
        Book ford = new Book("Ford", "2014 Tauros","",10.0);
        Book audi = new Book("Audi", "Audi A8","",5.0);
        Book bmw = new Book("BMW", "BMW M4","",1.0);

        OrderStats orderStats1 = new OrderStats(ford,1,10);
        OrderStats orderStats2 = new OrderStats(audi,1,4);
        OrderStats orderStats3 = new OrderStats(bmw,2,4);

        entityManager.persist(ford);
        entityManager.persist(bmw);
        entityManager.persist(audi);
        entityManager.persist(orderStats1);
        entityManager.persist(orderStats2);
        entityManager.persist(orderStats3);
        entityManager.flush();

        List<OrderStats> orderStats = orderRepository.findByOrderId(1);

        assertThat(orderStats).hasSize(2).extracting(OrderStats::getOrderId).containsOnly(1);
        assertThat(orderStats).hasSize(2).extracting(OrderStats::getBook).containsOnly(ford,audi);
    }
}
