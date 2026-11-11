
package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.model.EBook;
import com.bookstore.model.PrintedBook;
import com.bookstore.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String listBooks(Model model,
                            @RequestParam(required = false) String search) {
        var books = bookService.getAllBooks();

        if (search != null && !search.isBlank()) {
            books = books.stream()
                    .filter(book ->
                            book.getTitle().toLowerCase()
                                    .contains(search.toLowerCase())
                                    || String.valueOf(book.getBookId()).equals(search))
                    .toList();
        }

        model.addAttribute("books", books);
        model.addAttribute("search", search == null ? "" : search);
        return "books";
    }

    @GetMapping("/books/new")
    public String showAddForm() {
        return "add-book";
    }

    @PostMapping("/books")
    public String addBook(@RequestParam int bookId,
                          @RequestParam String type,
                          @RequestParam String title,
                          @RequestParam String author,
                          @RequestParam String category,
                          @RequestParam double price,
                          Model model) {
        if (bookId <= 0 || title.isBlank() || author.isBlank()
                || category.isBlank() || price < 0) {
            model.addAttribute("error", "Enter valid book details.");
            return "add-book";
        }

        Book book = type.equals("EBOOK")
                ? new EBook(bookId, title, author, category, price)
                : new PrintedBook(bookId, title, author, category, price);

        if (!bookService.addBook(book)) {
            model.addAttribute("error", "Book ID already exists.");
            return "add-book";
        }

        return "redirect:/books";
    }

    @GetMapping("/books/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model) {
        Book book = bookService.findBookById(id);

        if (book == null) {
            return "redirect:/books";
        }

        model.addAttribute("book", book);
        return "edit-book";
    }

    @PostMapping("/books/{id}/edit")
    public String updateBook(@PathVariable int id,
                             @RequestParam String title,
                             @RequestParam String author,
                             @RequestParam String category,
                             @RequestParam double price) {
        if (title.isBlank() || author.isBlank()
                || category.isBlank() || price < 0) {
            return "redirect:/books/" + id + "/edit?error";
        }

        bookService.updateBook(id, title, author, category, price);
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
