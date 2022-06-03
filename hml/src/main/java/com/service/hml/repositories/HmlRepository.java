package com.service.hml.repositories;

import com.service.hml.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HmlRepository extends JpaRepository<Book, Long> {

    Book findByTitle(String title);
    List<Book> findAll();
}
