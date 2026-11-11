
package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.repository.BookFileHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookFileHandler fileHandler;

    public BookService(BookFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public List<Book> getAllBooks() {
        return fileHandler.readBooks();
    }

    public boolean addBook(Book book) {
        List<Book> books = fileHandler.readBooks();

        for (Book existing : books) {
            if (existing.getBookId() == book.getBookId()) {
                return false;
            }
        }

        books.add(book);
        fileHandler.writeBooks(books);
        return true;
    }

    public Book findBookById(int bookId) {
        for (Book book : fileHandler.readBooks()) {
            if (book.getBookId() == bookId) {
                return book;
            }
        }

        return null;
    }

    public boolean updateBook(int bookId, String title,
                              String author, String category,
                              double price) {
        List<Book> books = fileHandler.readBooks();

        for (Book book : books) {
            if (book.getBookId() == bookId) {
                book.setTitle(title);
                book.setAuthor(author);
                book.setCategory(category);
                book.setPrice(price);

                fileHandler.writeBooks(books);
                return true;
            }
        }

        return false;
    }

    public boolean deleteBook(int bookId) {
        List<Book> books = fileHandler.readBooks();

        boolean removed = books.removeIf(
                book -> book.getBookId() == bookId
        );

        if (removed) {
            fileHandler.writeBooks(books);
        }

        return removed;
    }
}
