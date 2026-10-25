
package com.bookstore.model;

public class PrintedBook extends Book {

    public PrintedBook(int bookId, String title, String author,
                       String category, double price) {
        super(bookId, title, author, category, price);
    }

    @Override
    public String displayDetails() {
        return "Printed Book: " + getTitle()
                + " | Author: " + getAuthor()
                + " | Price: " + getPrice();
    }
}
