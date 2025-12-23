package com.uap.repository;

import com.uap.model.Contact;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CsvContactRepository implements ContactRepository {
	private final Path filePath;
	private final Map<String, Contact> store = new ConcurrentHashMap<>();
	private static final String HEADER = "id,name,phone,email,category,favorite";

	public CsvContactRepository(String filepath) throws IOException {
		this.filePath = Paths.get(filepath);
		init();
		load();
	}

	private void init() throws IOException {
		Path parent = filePath.getParent();
		if (parent != null && !Files.exists(parent)) {
			Files.createDirectories(parent);
		}
		if (!Files.exists(filePath)) {
			Files.write(filePath, Collections.singleton(HEADER), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
		}
	}

	private void load() throws IOException {
		List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		for (int i = 1; i < lines.size(); i++) {
			String line = lines.get(i).trim();
			if (line.isEmpty()) continue;
			String[] cols = parseCsvLine(line);
			Contact c = Contact.fromCsvRow(cols);
			if (c.getId() != null && !c.getId().isEmpty()) {
				store.put(c.getId(), c);
			}
		}
	}

	private synchronized void persist() throws IOException {
		List<String> lines = new ArrayList<>();
		lines.add(HEADER);
		lines.addAll(store.values().stream().map(Contact::toCsvRow).collect(Collectors.toList()));
		Files.write(filePath, lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
	}

	private String[] parseCsvLine(String line) {
		// split respecting quoted fields
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

	@Override
	public List<Contact> findAll() {
		return new ArrayList<>(store.values());
	}

	@Override
	public Optional<Contact> findById(String id) {
		return Optional.ofNullable(store.get(id));
	}

	@Override
	public synchronized Contact save(Contact contact) {
		// create or update
		if (contact.getId() == null || contact.getId().trim().isEmpty()) {
			contact.setId(UUID.randomUUID().toString());
		}
		store.put(contact.getId(), contact);
		try {
			persist();
		} catch (IOException e) {
			throw new RuntimeException("Failed to persist contacts: " + e.getMessage(), e);
		}
		return contact;
	}

	@Override
	public synchronized void delete(String id) {
		store.remove(id);
		try {
			persist();
		} catch (IOException e) {
			throw new RuntimeException("Failed to persist contacts: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Contact> search(String query) {
		if (query == null || query.trim().isEmpty()) return findAll();
		String q = query.toLowerCase();
		return store.values().stream()
			.filter(c -> (c.getName() != null && c.getName().toLowerCase().contains(q)) ||
						 (c.getPhone() != null && c.getPhone().toLowerCase().contains(q)))
			.collect(Collectors.toList());
	}

	@Override
	public List<Contact> filterByCategory(String category) {
		if (category == null || category.trim().isEmpty()) return findAll();
		String cat = category.toLowerCase();
		return store.values().stream()
			.filter(c -> c.getCategory() != null && c.getCategory().toLowerCase().equals(cat))
			.collect(Collectors.toList());
	}

	@Override
	public List<Contact> favorites() {
		return store.values().stream().filter(Contact::isFavorite).collect(Collectors.toList());
	}
}