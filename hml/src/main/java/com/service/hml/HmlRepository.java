package com.service.hml;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HmlRepository extends JpaRepository<Book, Long> {

    Book findByTitle(String title);
    List<Book> findAll();
}
