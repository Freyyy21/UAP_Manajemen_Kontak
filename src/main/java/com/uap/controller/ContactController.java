package com.uap.controller;

import com.uap.controller.Service.UserService;
import com.uap.model.Contact;
import com.uap.util.ValidationException;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.Optional;

public class ContactController {
    private final UserService userService;

    public ContactController() {
        this.userService = new UserService();
        initializeDefaultContacts();
    }

    private void initializeDefaultContacts() {
        // Add default contacts if none exist
        if (userService.listContacts().isEmpty()) {
            userService.createContact("John Doe", "+1234567890", "john@example.com", "Friend", true);
            userService.createContact("Jane Smith", "+0987654321", "jane@example.com", "Family", false);
            userService.createContact("Bob Wilson", "+1122334455", "bob@example.com", "Work", true);
            userService.createContact("Alice Brown", "+5544332211", "alice@example.com", "School", false);
            userService.createContact("Charlie Davis", "+6677889900", "charlie@example.com", "Friend", false);
        }
    }

    public List<Contact> getAllContacts() {
        return userService.listContacts();
    }

    public Optional<Contact> getContactById(String id) {
        return userService.getContact(id);
    }

    public Contact createContact(String name, String phone, String email, String category, boolean favorite) {
        try {
            return userService.createContact(name, phone, email, category, favorite);
        } catch (ValidationException e) {
            throw e;
        }
    }

    public Contact updateContact(String id, String name, String phone, String email, String category, Boolean favorite) {
        try {
            return userService.updateContact(id, name, phone, email, category, favorite);
        } catch (ValidationException e) {
            throw e;
        }
    }

    public void deleteContact(String id) {
        try {
            userService.deleteContact(id);
        } catch (ValidationException e) {
            throw e;
        }
    }

    public List<Contact> searchContacts(String query) {
        return userService.searchContacts(query);
    }

    public List<Contact> filterByCategory(String category) {
        if (category == null || category.equals("All")) {
            return getAllContacts();
        }
        return userService.filterByCategory(category);
    }

    public List<Contact> getFavorites() {
        return userService.favorites();
    }

    public Contact toggleFavorite(String id) {
        try {
            return userService.toggleFavorite(id);
        } catch (ValidationException e) {
            throw e;
        }
    }
}