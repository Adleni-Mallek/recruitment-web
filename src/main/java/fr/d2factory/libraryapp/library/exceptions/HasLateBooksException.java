package fr.d2factory.libraryapp.library.exceptions;

/**
 * This exception is thrown when a member who owns late books tries to borrow
 * another book
 */
public class HasLateBooksException extends RuntimeException {

	/**
	 * @author adleni
	 */
	private static final long serialVersionUID = 1L;

	public HasLateBooksException(String message) {
		super(message);
	}
}
