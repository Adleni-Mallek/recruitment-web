package fr.d2factory.libraryapp.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.library.exceptions.BookNotFoundException;
import fr.d2factory.libraryapp.library.exceptions.HasLateBooksException;
import fr.d2factory.libraryapp.member.Member;

/**
 * Class implement interface Library
 * 
 * @author adleni
 *
 * 
 */
public class LibraryImplement implements Library {

	private BookRepository bookRepository;

	/**
	 * Constructor
	 * @param bookRepository
	 */
	public LibraryImplement(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {

		member.getlistOfBorrowedBooks().forEach(item -> {
			LocalDate borrowedDate = bookRepository.findBorrowedBookDate(item.getIsbn());

			if (borrowedDate != null
					&& ChronoUnit.DAYS.between(borrowedDate, LocalDate.now()) > member.getMaxDaysToKeepBook()) {
				throw new HasLateBooksException("the member has Late Books ");
			}
		});
		

		
		Book borrowedBook = bookRepository.findBook(isbnCode);
		
		if (borrowedBook == null) {
			throw new BookNotFoundException("book not found");
		}
		bookRepository.saveBookBorrow(borrowedBook, borrowedAt);
		member.getlistOfBorrowedBooks().add(borrowedBook);
		return borrowedBook;
	}

	@Override
	public void returnBook(Book book, Member member) {

		LocalDate borrowedDate = bookRepository.findBorrowedBookDate(book.getIsbn());
		int numberOfDays = (int) ChronoUnit.DAYS.between(borrowedDate, LocalDate.now());
		member.payBook(numberOfDays);
	}

}
