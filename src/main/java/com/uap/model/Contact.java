package com.uap.model;

import java.util.UUID;

// Simple contact model with CSV helpers
public class Contact {
	private String id;
	private String name;
	private String phone;
	private String email;
	private String category;
	private boolean favorite;

	public Contact(String name, String phone, String email, String category, boolean favorite) {
		this(UUID.randomUUID().toString(), name, phone, email, category, favorite);
	}

	// full constructor (used when loading from CSV)
	public Contact(String id, String name, String phone, String email, String category, boolean favorite) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.category = category;
		this.favorite = favorite;
	}

	// getters & setters
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getCategory() { return category; }
	public void setCategory(String category) { this.category = category; }

	public boolean isFavorite() { return favorite; }
	public void setFavorite(boolean favorite) { this.favorite = favorite; }


	// CSV representation (escape quotes)
	private String escape(String s) {
		if (s == null) return "";
		return "\"" + s.replace("\"", "\"\"") + "\"";
	}

	public String toCsvRow() {
		return String.join(",",
			escape(id),
			escape(name),
			escape(phone),
			escape(email),
			escape(category),
			escape(Boolean.toString(favorite))
		);
	}

	public static Contact fromCsvRow(String[] cols) {
		// Expect cols length >= 6
		// mempetakan kolom CSV ke properti kontak
		String id = cols.length > 0 ? unquote(cols[0]) : "";
		String name = cols.length > 1 ? unquote(cols[1]) : "";
		String phone = cols.length > 2 ? unquote(cols[2]) : "";
		String email = cols.length > 3 ? unquote(cols[3]) : "";
		String category = cols.length > 4 ? unquote(cols[4]) : "";
		boolean favorite = cols.length > 5 ? Boolean.parseBoolean(unquote(cols[5])) : false;
		return new Contact(id, name, phone, email, category, favorite);
	}

	private static String unquote(String s) {
		if (s == null) return "";
		s = s.trim();
		if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
			s = s.substring(1, s.length() - 1).replace("\"\"", "\"");
		}
		return s;
	}
}
