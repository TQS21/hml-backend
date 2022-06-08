package com.service.hml.entities;

public class BookDTO {
    private int ID;

    private String title;

    private String author;

    private String coverUrlPath;

    private Double price;


    public static BookDTO fromBookEntity(Book book){
        return new BookDTO(book.getID(), book.getTitle(), book.getAuthor(), book.getCoverUrlPath(), book.getPrice());
    }

    public Book toBookEntity(){
        return new Book(this.ID, this.title, this.author, this.coverUrlPath, this.price);
    }

    public BookDTO(int ID, String title, String author, String coverUrlPath, Double price){
        this.ID = ID;
        this.title = title;
        this.author = author;
        this.coverUrlPath = coverUrlPath;
        this.price = price;
    }
}
