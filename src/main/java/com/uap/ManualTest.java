package com.uap;

import com.uap.controller.Service.UserService;
import com.uap.model.Contact;
import com.uap.repository.CsvContactRepository;
import com.uap.util.ValidationException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// Simple manual tester: jalankan main() dari IDE atau `java` untuk mengecek backend bekerja
public class ManualTest {
	// ...existing code...
	public static void main(String[] args) throws Exception {
		Path tmp = Files.createTempDirectory("contacts-manual-");
		String csvPath = tmp.resolve("contacts.csv").toString();
		CsvContactRepository repo = new CsvContactRepository(csvPath);
		UserService service = new UserService(repo);

		try {
			System.out.println("-> Create contacts");
			Contact alice = service.createContact("Alice", "0812345678", "alice@example.com", "friend", false);
			Contact bob = service.createContact("Bob", "081111", "bob@example.com", "family", true);
			assertNotNull(alice.getId(), "Alice id must not be null");

			System.out.println("-> List contacts");
			List<Contact> all = service.listContacts();
			assertEquals(2, all.size(), "Expect 2 contacts");

			System.out.println("-> Search 'ali'");
			List<Contact> s = service.searchContacts("ali");
			assertEquals(1, s.size(), "Expect 1 search result");

			System.out.println("-> Filter by category 'family'");
			List<Contact> fam = service.filterByCategory("family");
			assertEquals(1, fam.size(), "Expect 1 family contact");

			System.out.println("-> Toggle favorite for Alice");
			Contact toggled = service.toggleFavorite(alice.getId());
			if (!toggled.isFavorite()) throw new RuntimeException("Expected Alice to become favorite");

			System.out.println("-> Update Bob name");
			Contact updated = service.updateContact(bob.getId(), "Bob Smith", null, null, null, null);
			if (!"Bob Smith".equals(updated.getName())) throw new RuntimeException("Update failed");

			System.out.println("-> Delete Alice");
			service.deleteContact(alice.getId());
			assertEquals(1, service.listContacts().size(), "Expect 1 contact after delete");

			System.out.println("-> Validation checks");
			boolean ok = false;
			try {
				service.createContact("", "0812", "no@mail.com", null, false);
			} catch (ValidationException ex) {
				ok = true;
				System.out.println("Expected validation exception: " + ex.getMessage());
			}
			if (!ok) throw new RuntimeException("Expected ValidationException for empty name");

			System.out.println("ALL MANUAL TESTS PASSED");
			System.out.println("Temporary CSV: " + csvPath);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("MANUAL TESTS FAILED: " + e.getMessage());
			System.exit(1);
		}
	}

	private static void assertNotNull(Object o, String msg) {
		if (o == null) throw new RuntimeException(msg);
	}

	private static void assertEquals(int expected, int actual, String msg) {
		if (expected != actual) throw new RuntimeException(msg + " (expected " + expected + " but was " + actual + ")");
	}
}