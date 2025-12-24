package com.uap.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Locale;

public class PasswordUtil {
	private static final int SALT_LEN = 16;

	public static String hash(String password) {
		try {
			byte[] salt = new byte[SALT_LEN];
			SecureRandom.getInstanceStrong().nextBytes(salt);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt);
			md.update(password.getBytes(StandardCharsets.UTF_8));
			byte[] digest = md.digest();
			return toHex(salt) + ":" + toHex(digest);
		} catch (Exception e) {
			throw new RuntimeException("Failed to hash password", e);
		}
	}

	public static boolean verify(String password, String stored) {
		try {
			if (stored == null || !stored.contains(":")) return false;
			String[] parts = stored.split(":", 2);
			byte[] salt = fromHex(parts[0]);
			byte[] expected = fromHex(parts[1]);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt);
			md.update(password.getBytes(StandardCharsets.UTF_8));
			byte[] actual = md.digest();
			return MessageDigest.isEqual(expected, actual);
		} catch (Exception e) {
			throw new RuntimeException("Failed to verify password", e);
		}
	}

	private static String toHex(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (byte x : b) sb.append(String.format(Locale.ROOT, "%02x", x));
		return sb.toString();
	}

	private static byte[] fromHex(String s) {
		int len = s.length();
		byte[] out = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			out[i / 2] = (byte) Integer.parseInt(s.substring(i, i + 2), 16);
		}
		return out;
	}
}