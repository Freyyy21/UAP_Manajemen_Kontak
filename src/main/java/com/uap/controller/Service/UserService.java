package com.uap.controller.Service;

import com.uap.model.Contact;
import com.uap.repository.ContactRepository;
import com.uap.repository.CsvContactRepository;
import com.uap.util.ValidationUtil;
import com.uap.util.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

// Simple backend service for managing contacts (no UI)
public class UserService {
	private final ContactRepository repo;

	// New constructor to allow injecting a custom repository (useful for tests)
	public UserService(ContactRepository repo) {
		this.repo = repo;
	}

	public UserService() {
		try {
			this.repo = new CsvContactRepository("data/contacts.csv");
		} catch (IOException e) {
			throw new RuntimeException("Failed to initialize repository: " + e.getMessage(), e);
		}
	}

	// Create a new contact
	public Contact createContact(String name, String phone, String email, String category, boolean favorite) {
		ValidationUtil.validateName(name);
		ValidationUtil.validatePhone(phone);
		ValidationUtil.validateEmail(email);
		Contact c = new Contact(name, phone, email, category, favorite);
		return repo.save(c);
	}

	// Read
	public List<Contact> listContacts() {
		return repo.findAll();
	}

	public Optional<Contact> getContact(String id) {
		return repo.findById(id);
	}

	// Update
	public Contact updateContact(String id, String name, String phone, String email, String category, Boolean favorite) {
		Contact existing = repo.findById(id).orElseThrow(() -> new ValidationException("Contact not found: " + id));
		if (name != null) {
			ValidationUtil.validateName(name);
			existing.setName(name);
		}
		if (phone != null) {
			ValidationUtil.validatePhone(phone);
			existing.setPhone(phone);
		}
		if (email != null) {
			ValidationUtil.validateEmail(email);
			existing.setEmail(email);
		}
		if (category != null) existing.setCategory(category);
		if (favorite != null) existing.setFavorite(favorite);
		return repo.save(existing);
	}

	// Delete
	public void deleteContact(String id) {
		if (!repo.findById(id).isPresent()) throw new ValidationException("Contact not found: " + id);
		repo.delete(id);
	}

	// Search & filter
	public List<Contact> searchContacts(String query) {
		return repo.search(query);
	}

	public List<Contact> filterByCategory(String category) {
		return repo.filterByCategory(category);
	}

	public List<Contact> favorites() {
		return repo.favorites();
	}

	public Contact toggleFavorite(String id) {
		Contact c = repo.findById(id).orElseThrow(() -> new ValidationException("Contact not found: " + id));
		c.setFavorite(!c.isFavorite());
		return repo.save(c);
	}
}
