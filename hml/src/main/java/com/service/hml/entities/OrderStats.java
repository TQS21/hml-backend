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

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book bookOrdered;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public OrderStats(){
    }

    public OrderStats(Book book, int orderId, int quantity) {
        this.bookOrdered = book;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return bookOrdered;
    }

    public void setBook(Book book) {
        this.bookOrdered = book;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderStats{" +
                "id=" + id +
                ", book=" + bookOrdered +
                ", orderId=" + orderId +
                ", quantity=" + quantity +
                '}';
    }
}
