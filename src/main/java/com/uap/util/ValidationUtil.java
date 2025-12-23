package com.uap.util;

import java.util.regex.Pattern;

public class ValidationUtil {
	private static final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);
	private static final Pattern PHONE = Pattern.compile("^\\+?[0-9\\- ]{6,20}$");

	public static void validateName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new ValidationException("Name must not be empty");
		}
	}

	public static void validateEmail(String email) {
		if (email == null || email.trim().isEmpty()) return; // optional
		if (!EMAIL.matcher(email).matches()) {
			throw new ValidationException("Invalid email format");
		}
	}

	public static void validatePhone(String phone) {
		if (phone == null || phone.trim().isEmpty()) {
			throw new ValidationException("Phone must not be empty");
		}
		if (!PHONE.matcher(phone).matches()) {
			throw new ValidationException("Phone must be numeric-ish and 6-20 chars");
		}
	}
}