package com.service.hml.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class OrderStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "quantity", nullable = false)
    private int quantity;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "available", nullable = false)
    private boolean available;

    @OneToMany(mappedBy = "bookHistory")
    @JsonIgnore
    Set<History> history = new HashSet<>();

    public OrderStats(){
    }
    
}
