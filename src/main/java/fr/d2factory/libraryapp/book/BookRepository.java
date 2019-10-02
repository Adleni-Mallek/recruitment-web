package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author adleni
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
	private Map<ISBN, Book> availableBooks = new HashMap<>();
	private Map<ISBN, LocalDate> borrowedBooks = new HashMap<>();
	

	/**
	 * add list of books
	 * 
	 * @param books
	 */
	public void addBooks(List<Book> books) {
		availableBooks = books.stream().collect(Collectors.toMap(Book::getIsbn, b -> b));
	}

	/**
	 * find a book in availableBooks
	 * 
	 * @param isbnCode
	 * @return book
	 */
	public Book findBook(long isbnCode) {
		
	 return	availableBooks.entrySet().stream()
		  .filter(e -> e.getKey().getIsbnCode() == isbnCode)
		  .map(Map.Entry::getValue)
		  .findFirst()
		  .orElse(null);
	}

	/**
	 * 
	 * @param book
	 * @param borrowedAt
	 * 
	 * save the book Borrow: borrowedBooks
	 * remove the book :  availableBooks
	 * 
	 */
	public void saveBookBorrow(Book book, LocalDate borrowedAt) {

		borrowedBooks.put(book.getIsbn(), borrowedAt);
		availableBooks.remove(book.getIsbn(), borrowedAt);
	}


	/**
	 * 
	 * @param isbn
	 * @return the date of the borrowed book
	 */
	public LocalDate findBorrowedBookDate(ISBN isbn) {
		return borrowedBooks.get(isbn);
	}
}
