package com.service.hml.entities;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int ID;

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

    public Book(){
    }

    public Book(String title, String author, String coverUrlPath, Double price){
        this.title = title;
        this.author = author;
        this.coverUrlPath = coverUrlPath;
        this.price = price;
        this.available = true;
    }

    public Book(int ID, String title, String author, String coverUrlPath, Double price){
        this.ID = ID;
        this.title = title;
        this.author = author;
        this.coverUrlPath = coverUrlPath;
        this.price = price;
        this.available = true;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    @Override
    public String toString() {
        return "Book{" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", coverUrlPath='" + coverUrlPath + '\'' +
                ", price=" + price +
                ", available=" + available +
                '}';
    }
}
