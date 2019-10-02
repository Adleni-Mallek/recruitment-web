package fr.d2factory.libraryapp.library.exceptions;

/**
 * @author adleni
 * This exception is thrown when the book is not found
 * 
 */
public class BookNotFoundException extends RuntimeException {

	/**
	 * @author adleni
	 */
	private static final long serialVersionUID = 1L;

	public BookNotFoundException(String message) {
		super(message);
	}
}
