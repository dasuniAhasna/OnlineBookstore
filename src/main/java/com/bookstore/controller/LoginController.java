package com.bookstore.controller;

import com.bookstore.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final BookService bookService;

    public LoginController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        if (username.equals("admin")
                && password.equals("admin123")) {
            return "redirect:/admin";
        }

        model.addAttribute(
                "error", "Invalid username or password.");

        return "login";
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute(
                "totalBooks",
                bookService.getAllBooks().size());

        return "admin";
    }
}