package com.uap;

import com.uap.controller.Service.UserService;
import com.uap.controller.Service.AuthService;
import com.uap.model.Contact;
import com.uap.model.User;
import com.uap.repository.CsvContactRepository;
import com.uap.repository.CsvUserRepository;
import com.uap.util.ValidationException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

// Simple manual tester: run main() from an IDE or `java` to sanity-check the backend
public class ManualTest {
	// ...existing code...
	public static void main(String[] args) throws Exception {
		// use a fixed test directory inside the project "data" folder
		Path dataDir = Paths.get("data");
		if (!Files.exists(dataDir)) {
			Files.createDirectories(dataDir);
		}
		Path tmp = dataDir.resolve("contacts-manual");
		if (!Files.exists(tmp)) {
			Files.createDirectories(tmp);
		}
		String csvPath = tmp.resolve("contacts.csv").toString();
		// force overwrite existing CSV so data won't accumulate across runs
		CsvContactRepository repo = new CsvContactRepository(csvPath, true);

	// create a per-test users CSV and AuthService (overwrite so repeated runs are deterministic)
	Path usersDir = dataDir.resolve("users-manual");
	if (!Files.exists(usersDir)) Files.createDirectories(usersDir);
	String usersCsv = usersDir.resolve("users.csv").toString();
	CsvUserRepository usersRepo = new CsvUserRepository(usersCsv, true);
	AuthService auth = new AuthService(usersRepo);

	System.out.println("-> Register user 'tester'");
	User user = auth.register("tester", "secret123");
	System.out.println("-> Registered: " + user.getUsername() + " (id=" + user.getId() + ")");

		System.out.println("-> Login user 'tester'");
		User logged = auth.login("tester", "secret123");

		System.out.println("-> Open per-user contact service (overwrite contacts)");
		UserService service = auth.openContactService(logged, true);

		try {
			System.out.println("-> Create contacts for user: " + logged.getUsername());
			Contact alice = service.createContact("Alice", "0812345678", "alice@example.com", "friend", false);
			Contact bob = service.createContact("Bob", "081111", "bob@example.com", "family", true);
			Contact carol = service.createContact("Carol", "082222", "carol@example.com", "work", false);
			assertNotNull(alice.getId(), "Alice id must not be null");

			System.out.println("-> List contacts");
			List<Contact> all = service.listContacts();
			assertEquals(3, all.size(), "Expect 3 contacts");

			System.out.println("-> Update Bob's name and phone");
			Contact updated = service.updateContact(bob.getId(), "Bobby", "081119", "bobby@example.com", "family", null);
			assertEquals("Bobby", updated.getName(), "Name should be updated");
			assertEquals("081119", updated.getPhone(), "Phone should be updated");

			System.out.println("-> Find by ID (Alice)");
			Contact found = service.getContact(alice.getId()).orElseThrow(() -> new RuntimeException("Alice not found"));
			assertEquals("Alice", found.getName(), "Alice name must match");

			System.out.println("-> Search contacts (query 'alice')");
			List<Contact> foundBySearch = service.searchContacts("alice");
			assertEquals(1, foundBySearch.size(), "Search should find 1 contact named alice");

			System.out.println("-> Filter by category 'family'");
			List<Contact> family = service.filterByCategory("family");
			assertEquals(1, family.size(), "Expect 1 family contact");

			System.out.println("-> Favorites before toggle");
			List<Contact> favs = service.favorites();
			assertEquals(1, favs.size(), "Expect 1 favorite initially");

			System.out.println("-> Toggle favorite for Alice");
			Contact toggled = service.toggleFavorite(alice.getId());
			if (!toggled.isFavorite()) throw new RuntimeException("Expected Alice to become favorite");
			assertEquals(2, service.favorites().size(), "Expect 2 favorites after toggling Alice");

			System.out.println("-> Delete Carol");
			service.deleteContact(carol.getId());
			assertEquals(2, service.listContacts().size(), "Expect 2 contacts after deleting Carol");
			if (service.getContact(carol.getId()).isPresent()) throw new RuntimeException("Carol should be deleted");

			System.out.println("-> Validation checks (invalid contact creations should fail)");
			assertThrows(() -> service.createContact("", "08", "invalid", "", false), ValidationException.class, "Empty name should fail");
			assertThrows(() -> service.createContact("Name", "bad-phone", "invalid@", "", false), ValidationException.class, "Bad phone/email should fail");

			System.out.println("-> ALL MANUAL USER TESTS PASSED");
			System.out.println("User contacts stored at data/users/" + logged.getUsername() + "/contacts.csv");
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

	private static void assertEquals(String expected, String actual, String msg) {
		if (expected == null && actual == null) return;
		if (expected != null && expected.equals(actual)) return;
		throw new RuntimeException(msg + " (expected '" + expected + "' but was '" + actual + "')");
	}

	private static void assertThrows(Runnable r, Class<? extends Exception> exClass, String msg) {
		try {
			r.run();
		} catch (Exception e) {
			if (exClass.isInstance(e)) return;
			throw new RuntimeException(msg + " - threw wrong exception: " + e.getClass().getName());
		}
		throw new RuntimeException(msg + " - expected exception " + exClass.getName() + " but none thrown");
	}
}