package com.uap.controller;

import com.uap.model.User;
import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Map;

public class LoginController {
    private static final Map<String, User> users = new HashMap<>();

    static {
        // Initialize default users
        users.put("fazel", new User(1L, "fazel", "fazel123"));
        users.put("faizul", new User(2L, "faizul", "faizul123"));
    }

    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong");
        }

        User user = users.get(username.toLowerCase());

        if (user == null) {
            throw new IllegalArgumentException("Username tidak ditemukan");
        }

        if (!user.getPasssword().equals(password)) {
            throw new IllegalArgumentException("Password salah");
        }

        return user;
    }
}