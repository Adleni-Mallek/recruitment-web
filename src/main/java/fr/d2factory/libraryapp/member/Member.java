package fr.d2factory.libraryapp.member;

import java.util.ArrayList;
import java.util.List;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.library.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library} A
 * member can be either a student or a resident
 */
public abstract class Member {
	/**
	 * An initial sum of money the member has
	 */
	private float wallet;

	protected int MaxDaysToKeepBook;

	protected boolean late;

	protected List<Book> listOfBorrowedBooks = new ArrayList<>();

	/**
	 * The member should pay their books when they are returned to the library
	 *
	 * @param numberOfDays the number of days they kept the book
	 */
	public abstract void payBook(int numberOfDays);

	public float getWallet() {
		return wallet;
	}

	public void setWallet(float wallet) {
		this.wallet = wallet;
	}

	public int getMaxDaysToKeepBook() {
		return MaxDaysToKeepBook;
	}

	public List<Book> getlistOfBorrowedBooks() {
		return listOfBorrowedBooks;
	}

	public Member(int wallet) {
		this.wallet = wallet;
	}

	public boolean isLate() {
		return this.late;
	}

}
