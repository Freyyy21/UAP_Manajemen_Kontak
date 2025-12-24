package com.uap.controller;

import com.uap.model.User;
import com.uap.repository.CsvUserRepository;
import com.uap.controller.Service.AuthService;

import java.util.HashMap;
import java.util.Map;

public class LoginController {
    private static final Map<String, User> users = new HashMap<>();
    private static final AuthService authService = new AuthService();
    private static final CsvUserRepository userRepo;


    static {
        // Initialize default users
        try {
            userRepo = new CsvUserRepository();
        } catch (Exception e) {
            throw new RuntimeException("Gagal menginisialisasi repositori pengguna: " + e.getMessage(), e);
        }

        // cek apakah ada sudah ada user untuk testing dengan nama 
        try {
            if (!userRepo.findByUsername("admin").isPresent()) {
                authService.register("admin", "admin123");
            }
        } catch (Exception ignored) {
           // kosongkan klo user sudah ada
        }

        try {
            if (!userRepo.findByUsername("user").isPresent()) {
                authService.register("user", "user123");
            }
        } catch (Exception ignored) {
           
        }

        for (User user : userRepo.findAll()) {
            users.put(user.getUsername().toLowerCase(), user);
        }
    }

    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong");
        }

        try {
            // menverifikasi user dengan password yang sudah di hash
            return authService.login(username, password);
        } catch (com.uap.util.ValidationException e) {
            // throw exception jika ada kesalahan verifikasi
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}