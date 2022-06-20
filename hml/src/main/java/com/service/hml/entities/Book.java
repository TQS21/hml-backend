package com.service.hml.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "cover", nullable = false)
    private String coverUrlPath;

    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "available", nullable = false)
    private boolean available;

    @OneToMany(mappedBy = "bookHistory")
    @JsonIgnore
    Set<History> history = new HashSet<>();

    @OneToMany(mappedBy = "bookOrdered")
    @JsonIgnore
    Set<OrderStats> orderStats = new HashSet<>();

    public Book(){
    }

    public Book(String title, String author, String coverUrlPath, Double price){
        this.title = title;
        this.author = author;
        this.coverUrlPath = coverUrlPath;
        this.price = price;
        this.available = true;
    }

    public Book(int id, String title, String author, String coverUrlPath, Double price){
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverUrlPath = coverUrlPath;
        this.price = price;
        this.available = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverUrlPath() {
        return coverUrlPath;
    }

    public void setCoverUrlPath(String coverUrlPath) {
        this.coverUrlPath = coverUrlPath;
    }

    public boolean isAvailable() {return available;}

    public void setAvailable(boolean available) {this.available = available;}

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<History> getHistory() {
        return history;
    }

    public void setHistory(Set<History> ordered) {
        this.history = ordered;
    }

    public void addHistory(History ordered) {
        this.history.add(ordered);
    }

    @Override
    public String toString() {
        return "Book{" +
                "ID=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", coverUrlPath='" + coverUrlPath + '\'' +
                ", price=" + price +
                ", available=" + available +
                '}';
    }
}
