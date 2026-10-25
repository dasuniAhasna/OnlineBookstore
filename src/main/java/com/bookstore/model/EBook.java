
package com.bookstore.model;

public class EBook extends Book {

    public EBook(int bookId, String title, String author,
                 String category, double price) {
        super(bookId, title, author, category, price);
    }

    @Override
    public String displayDetails() {
        return "E-Book: " + getTitle()
                + " | Author: " + getAuthor()
                + " | Price: " + getPrice();
    }
}
