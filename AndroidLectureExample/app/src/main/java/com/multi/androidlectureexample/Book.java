package com.multi.androidlectureexample;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public class Book {

    private String isbn;
    private String img;
    private String title;
    private String author;
    private String price;

    public Book() {
    }

    public Book(String isbn, String title) {
        super();
        this.isbn = isbn;
        this.title = title;
    }

    public Book(String isbn, String img, String title, String author, String price) {
        super();
        this.isbn = isbn;
        this.img = img;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @NonNull
    @Override
    public String toString() {
        return "title : " + this.getTitle() + " , isbn : " + this.getIsbn() + ", author : "
                + this.getAuthor() + " , price : " + this.getPrice() + ", img : " + this.getImg();
    }
}