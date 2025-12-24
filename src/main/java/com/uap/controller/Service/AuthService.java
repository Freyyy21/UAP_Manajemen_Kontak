package com.uap.controller.Service;

import com.uap.model.User;
import com.uap.repository.CsvContactRepository;
import com.uap.repository.CsvUserRepository;
import com.uap.repository.UserRepository;
import com.uap.util.PasswordUtil;
import com.uap.util.ValidationException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

// Authentication service: register/login users and open per-user contact service
public class AuthService {
	private final UserRepository userRepo;

	// default constructor (uses data/users.csv)
	public AuthService() {
		try {
			this.userRepo = new CsvUserRepository("data/users.csv");
		} catch (IOException e) {
			throw new RuntimeException("Failed to init user repo: " + e.getMessage(), e);
		}
	}

	// test-friendly constructor
	public AuthService(UserRepository repo) {
		this.userRepo = repo;
	}

	public User register(String username, String plainPassword) {
		if (username == null || username.trim().isEmpty()) throw new ValidationException("Username required");
		if (plainPassword == null || plainPassword.isEmpty()) throw new ValidationException("Password required");
		Optional<User> existing = userRepo.findByUsername(username);
		if (existing.isPresent()) throw new ValidationException("Username already exists");
		String hashed = PasswordUtil.hash(plainPassword);
		User u = new User(null, username, hashed);
		return userRepo.save(u);
	}

	public User login(String username, String plainPassword) {
		Optional<User> user = userRepo.findByUsername(username);
		if (!user.isPresent()) throw new ValidationException("Invalid username or password");
		User u = user.get();
		if (!PasswordUtil.verify(plainPassword, u.getPassword())) throw new ValidationException("Invalid username or password");
		return u;
	}

	// Buka UserService yang menyimpan kontak di data/users/{username}/contacts.csv
	public UserService openContactService(User user, boolean overwriteContacts) {
		try {
			String safe = sanitizeUsername(user.getUsername());
			Path p = Paths.get("data").resolve("users").resolve(safe).resolve("contacts.csv");
			CsvContactRepository repo = new CsvContactRepository(p.toString(), overwriteContacts);
			return new UserService(repo);
		} catch (IOException e) {
			throw new RuntimeException("Failed to open user contact service: " + e.getMessage(), e);
		}
	}

	private String sanitizeUsername(String s) {
		return s == null ? "unknown" : s.replaceAll("[^a-zA-Z0-9_\\-]", "_");
	}
}
