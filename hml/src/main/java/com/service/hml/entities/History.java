package com.service.hml.entities;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int historyId;

    @ManyToOne
    @JoinColumn(name = "user_email")
    @JsonIgnore
    private User userHistory;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book bookHistory;

    public History() {
    }
    public History(User user, Book book) {
        this.userHistory = user;
        this.bookHistory = book;
    }

    public User getUser() {
        return userHistory;
    }

    public void setUser(User user) {
        this.userHistory = user;
    }

    public Book getBook() {
        return bookHistory;
    }

    public void setBook(Book book) {
        this.bookHistory = book;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + historyId +
                ", user=" + userHistory +
                ", book=" + bookHistory +
                '}';
    }
}
