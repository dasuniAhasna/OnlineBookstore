
package com.bookstore.repository;

import com.bookstore.model.Book;
import com.bookstore.model.EBook;
import com.bookstore.model.PrintedBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BookFileHandler {
    private final Path filePath =
            Paths.get("books.txt").toAbsolutePath();

    public List<Book> readBooks() {
        List<Book> books = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return books;
        }

        try {
            for (String line : Files.readAllLines(filePath)) {
                String[] data = line.split("\\|", -1);

                if (data.length != 6) {
                    continue;
                }

                int id = Integer.parseInt(data[0]);
                String type = data[1];
                String title = data[2];
                String author = data[3];
                String category = data[4];
                double price = Double.parseDouble(data[5]);

                if (type.equals("EBOOK")) {
                    books.add(new EBook(
                            id, title, author, category, price));
                } else if (type.equals("PRINTED")) {
                    books.add(new PrintedBook(
                            id, title, author, category, price));
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new IllegalStateException(
                    "Could not read the books file.", e);
        }

        return books;
    }

    public void writeBooks(List<Book> books) {
        List<String> lines = new ArrayList<>();

        for (Book book : books) {
            String type = book instanceof EBook
                    ? "EBOOK" : "PRINTED";

            String line = book.getBookId() + "|"
                    + type + "|"
                    + clean(book.getTitle()) + "|"
                    + clean(book.getAuthor()) + "|"
                    + clean(book.getCategory()) + "|"
                    + book.getPrice();

            lines.add(line);
        }

        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(
                    filePath,
                    lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not save the books file.", e);
        }
    }

    private String clean(String value) {
        return value == null
                ? "" : value.replace("|", "/")
                .replace("\n", " ")
                .replace("\r", " ");
    }
}
