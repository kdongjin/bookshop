package bookStore;

import java.io.Serializable;
import java.util.Objects;

public class Books implements Comparable<Books>, Serializable {
	// filed
	private String isbn;
	private String bookTitle;
	private String authorName;
	private String publisherName;
	private String date;
	private int price;
	private int stock;

	// constructor
	public Books() {
		this(null, null, null, null, null, 0, 0);
	}

	public Books(String isbn, String bookTitle, String authorName, String publisherName, String date, int price,
			int stock) {
		super();
		this.isbn = isbn;
		this.bookTitle = bookTitle;
		this.authorName = authorName;
		this.publisherName = publisherName;
		this.date = date;
		this.price = price;
		this.stock = stock;
	}

	// method
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;

	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Books)) {
			return false;
		}
		Books books = (Books) obj;
		if (this.isbn.equals(books.isbn)) {
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(Books o) {
		return this.isbn.compareToIgnoreCase(o.isbn);
	}

	@Override
	public String toString() {
		return isbn + "\t" + bookTitle + "\t 재고" + stock + "권\t"
				+ price + "원 \t"+ authorName +"\t" + publisherName + "\t" + date;
	}

}
