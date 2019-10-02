package fr.d2factory.libraryapp.book;

/**
 * @author adleni
 * 
 * A simple representation of a book
 */
public class Book {
	private String title;
	private String author;
	private ISBN isbn;

	public Book(String title, String author, ISBN isbn) {
		this.setTitle(title);
		this.setAuthor(author);
		this.isbn = isbn;
	}

	public Book() {
	}

	public ISBN getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
