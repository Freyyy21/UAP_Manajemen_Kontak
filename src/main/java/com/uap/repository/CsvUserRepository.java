package com.uap.repository;

import com.uap.model.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CsvUserRepository implements UserRepository {
	private Path filePath = Paths.get("./Data/users.csv");
	private final Map<String, User> store = new ConcurrentHashMap<>();
	private static final String HEADER = "id,username,password";

	public CsvUserRepository(boolean overwrite) throws IOException {
		this.filePath = Paths.get(filePath.toString());
		init();
		if (overwrite && Files.exists(filePath)) {
			Files.write(filePath, Collections.singleton(HEADER), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			store.clear();
		} else {
			if (!Files.exists(filePath)) {
				Files.write(filePath, Collections.singleton(HEADER), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
			}
			load();
		}
	}

	public CsvUserRepository() throws IOException {
		this(false);
	}

	private void init() throws IOException {
		Path parent = filePath.getParent();
		if (parent != null && !Files.exists(parent)) Files.createDirectories(parent);
	}

	private void load() throws IOException {
		List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		for (int i = 1; i < lines.size(); i++) {
			String line = lines.get(i).trim();
			if (line.isEmpty()) continue;
			String[] cols = parseCsvLine(line);
			String id = cols.length > 0 ? unquote(cols[0]) : "";
			String username = cols.length > 1 ? unquote(cols[1]) : "";
			String password = cols.length > 2 ? unquote(cols[2]) : "";
			if (!id.isEmpty()) store.put(id, new User(id, username, password));
		}
	}

	private synchronized void persist() throws IOException {
		List<String> lines = new ArrayList<>();
		lines.add(HEADER);
		for (User u : store.values()) {
			lines.add(escape(u.getId()) + "," + escape(u.getUsername()) + "," + escape(u.getPassword()));
		}
		Files.write(filePath, lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(store.values());
	}

	@Override
	public Optional<User> findById(String id) {
		return Optional.ofNullable(store.get(id));
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return store.values().stream().filter(u -> u.getUsername() != null && u.getUsername().equals(username)).findFirst();
	}

	@Override
	public synchronized User save(User user) {
		if (user.getId() == null || user.getId().trim().isEmpty()) {
			user.setId(UUID.randomUUID().toString());
		}
		store.put(user.getId(), user);
		try {
			persist();
		} catch (IOException e) {
			throw new RuntimeException("Failed to persist users: " + e.getMessage(), e);
		}
		return user;
	}

	@Override
	public synchronized void delete(String id) {
		store.remove(id);
		try {
			persist();
		} catch (IOException e) {
			throw new RuntimeException("Failed to persist users: " + e.getMessage(), e);
		}
	}

	// helpers: naive CSV field handling similar to other repo
	private String escape(String s) {
		if (s == null) return "";
		return "\"" + s.replace("\"", "\"\"") + "\"";
	}

	private String unquote(String s) {
		if (s == null) return "";
		s = s.trim();
		if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
			s = s.substring(1, s.length() - 1).replace("\"\"", "\"");
		}
		return s;
	}

	private String[] parseCsvLine(String line) {
		List<String> cols = new ArrayList<>();
		boolean inQuotes = false;
		StringBuilder cur = new StringBuilder();
		for (int i = 0; i < line.length(); i++) {
			char ch = line.charAt(i);
			if (ch == '"') {
				inQuotes = !inQuotes;
				cur.append(ch);
			} else if (ch == ',' && !inQuotes) {
				cols.add(cur.toString());
				cur.setLength(0);
			} else {
				cur.append(ch);
			}
		}
		cols.add(cur.toString());
		return cols.toArray(new String[0]);
	}
}