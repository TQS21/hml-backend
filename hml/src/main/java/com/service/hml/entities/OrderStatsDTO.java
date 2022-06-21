package com.service.hml.entities;

public class OrderStatsDTO {

    private Book book;

    private int number;

    public OrderStatsDTO(Book book, int number) {
        this.book = book;
        this.number = number;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
