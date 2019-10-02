package fr.d2factory.libraryapp.library;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.configuration.Constants;
import fr.d2factory.libraryapp.library.exceptions.HasLateBooksException;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;
import fr.d2factory.libraryapp.member.exception.InsufficientFundsException;

/**
 * 
 * @author adleni
 *
 */
public class LibraryTest {
	private Library library;
	private BookRepository bookRepository;

	private List<Book> listOfBooks;

	@Before
	public void setup() throws URISyntaxException {
		// TODO instantiate the library and the repository

		bookRepository = new BookRepository();
		library = new LibraryImplement(bookRepository);

		// TODO add some test books (use BookRepository#addBooks)
		// TODO to help you a file called books.json is available in src/test/resources
		// JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();

		Path path = Paths.get(getClass().getClassLoader().getResource("books.json").toURI());

		try (FileReader reader = new FileReader(path.toString())) {

			// Read JSON file
			Object obj = jsonParser.parse(reader);

			JSONArray list = (JSONArray) obj;

			listOfBooks = new ArrayList<>();
			for (Object o : list) {
				JSONObject bookJson = (JSONObject) o;
				String title = (String) bookJson.get("title");
				String author = (String) bookJson.get("author");
				long isbn = (long) ((JSONObject) bookJson.get("isbn")).get("isbnCode");

				Book book = new Book(title, author, new ISBN(isbn));

				listOfBooks.add(book);
			}

			bookRepository.addBooks(listOfBooks);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 30 days * 10 cents = 300 cents
	 */
	@Test
	public void students_pay_10_cents_the_first_30days() {
		// fail("Implement me");

		int wallet = 500;

		Member student = new Student(wallet, false);

		library.borrowBook(listOfBooks.get(0).getIsbn().getIsbnCode(), student, LocalDate.now().minusDays(30));
		library.returnBook(listOfBooks.get(0), student);
		Assert.assertEquals(200, student.getWallet(), 0);
	}

	/**
	 * 20 days * 10 cents = 200 cents
	 */
	@Test
	public void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
		// fail("Implement me");

		int wallet = 500;

		Member resident = new Resident(wallet);

		library.borrowBook(listOfBooks.get(0).getIsbn().getIsbnCode(), resident, LocalDate.now().minusDays(20));
		library.returnBook(listOfBooks.get(0), resident);
		Assert.assertEquals(300, resident.getWallet(), 0);
	}

	/**
	 * 
	 */
	@Test(expected = HasLateBooksException.class)
	public void members_cannot_borrow_book_if_they_have_late_books() {
		// fail("Implement me");

		int wallet = 500;

		// For Student
		Member student = new Student(wallet, false);
		library.borrowBook(listOfBooks.get(0).getIsbn().getIsbnCode(), student,
				LocalDate.now().minusDays(Constants.STUDENT_MAX_DAYS_BEFORE_LATE + 1));
		library.borrowBook(listOfBooks.get(1).getIsbn().getIsbnCode(), student, LocalDate.now());

		// For Resident
		Member resident = new Resident(wallet);
		library.borrowBook(listOfBooks.get(0).getIsbn().getIsbnCode(), resident,
				LocalDate.now().minusDays(Constants.RESIDENT_MAX_DAYS_BEFORE_LATE + 1));
		library.borrowBook(listOfBooks.get(1).getIsbn().getIsbnCode(), resident, LocalDate.now());
	}

	/**
	 * 
	 */
	@Test
	public void borrowed_book_is_no_longer_available() {
		// fail("Implement me");

		long isbnCode = 3326456467000L;

		Book book = bookRepository.findBook(isbnCode);

		Assert.assertEquals(book, null);
	}

	/**
	 * 
	 */
	@Test
	public void students_in_1st_year_are_not_taxed_for_the_first_15days() {
		// fail("Implement me");

		int wallet = 500;

		Member student = new Student(wallet, true);

		library.borrowBook(listOfBooks.get(0).getIsbn().getIsbnCode(), student,
				LocalDate.now().minusDays(Constants.STUDENT_NUMBER_DAYS_FIRST_YEAR_FREE));

		library.returnBook(listOfBooks.get(0), student);

		Assert.assertEquals(wallet, student.getWallet(), 0);

	}

	/**
	 * 
	 */
	@Test
	public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days() {
		// fail("Implement me");

		int wallet = 500;
		Member studient = new Student(wallet, false);

		int dayskeep = Constants.STUDENT_MAX_DAYS_BEFORE_LATE + 2;

		library.borrowBook(listOfBooks.get(0).getIsbn().getIsbnCode(), studient, LocalDate.now().minusDays(dayskeep));
		library.returnBook(listOfBooks.get(0), studient);

		int pay = Constants.STUDENT_MAX_DAYS_BEFORE_LATE * Constants.STUDENT_PRICE_BEFORE_LATE
				+ (dayskeep - Constants.STUDENT_MAX_DAYS_BEFORE_LATE) * Constants.STUDENT_PRICE_DAY_AFTER_LATE;

		Assert.assertEquals(500 - pay, studient.getWallet(), 0);

	}

	/**
	 * 
	 */
	@Test
	public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
		// fail("Implement me");

		int wallet = 1000;
		Member resident = new Resident(wallet);

		int daysKeep = Constants.RESIDENT_MAX_DAYS_BEFORE_LATE + 2;

		library.borrowBook(listOfBooks.get(0).getIsbn().getIsbnCode(), resident, LocalDate.now().minusDays(daysKeep));
		library.returnBook(listOfBooks.get(0), resident);

		int pay = (Constants.RESIDENT_MAX_DAYS_BEFORE_LATE * Constants.RESIDENT_PRICE_DAY_BEFORE_LATE)
				+ (daysKeep - Constants.RESIDENT_MAX_DAYS_BEFORE_LATE) * Constants.RESIDENT_PRICE_DAY_AFTER_LATE;

		Assert.assertEquals(wallet - pay, resident.getWallet(), 0);

	}

	/**
	 * 
	 */
	@Test(expected = InsufficientFundsException.class)
	public void membre_insufficient_funds() {
		// fail("Implement me");
		int wallet = 500;
		Member resident = new Resident(wallet);

		int daysKeep = Constants.RESIDENT_MAX_DAYS_BEFORE_LATE + 2;

		library.borrowBook(listOfBooks.get(0).getIsbn().getIsbnCode(), resident, LocalDate.now().minusDays(daysKeep));
		library.returnBook(listOfBooks.get(0), resident);
	}

}
