package com.service.hml.entities;

public class BookDTO {
    private int ID;

    private String title;

    private String author;

    private String coverUrlPath;

    public BookDTO fromBookEntity(Book book){
        return new BookDTO(book.getID(), book.getTitle(), book.getAuthor(), book.getCoverUrlPath());
    }

    public Book toBookEntity(){
        return new Book(this.ID, this.title, this.author, this.coverUrlPath);
    }

    public BookDTO(int ID, String title, String author, String coverUrlPath){
        this.ID = ID;
        this.title = title;
        this.author = author;
        this.coverUrlPath = coverUrlPath;
    }
}
