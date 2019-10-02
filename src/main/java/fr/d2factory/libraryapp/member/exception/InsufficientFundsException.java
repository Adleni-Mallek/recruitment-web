package fr.d2factory.libraryapp.member.exception;

public class InsufficientFundsException extends RuntimeException {

	/**
	 * This exception is thrown when insufficient funds
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsufficientFundsException(String message) {
		super(message);
	}

}
