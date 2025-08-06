package com.example.demo.model;

public class News {
    private String author;
    private String title;
    private String description;

    // Default constructor
    public News() {
    }

    // Parameterized constructor
    public News(String author, String title, String description) {
        this.author = author;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // toString method
    @Override
    public String toString() {
        return "News{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}