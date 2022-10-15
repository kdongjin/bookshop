package bookStore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConnection {
	private Connection connection = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public void connect() {
		Properties properties = new Properties();
		InputStream fis;
		try {
			fis = new FileInputStream(
					"C:\\Users\\USER\\java\\eclipse\\eclipse-workspace\\git_BookStoreProject\\src\\bookStore\\properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("file not found." +e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		try {
			Class.forName(properties.getProperty("driver"));
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("userid"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("path not found." +e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public int insert(Books books) {
		int returnValue = -1;
		String insertQuery = "insert into booksTBL values(?, ?, ?, ?, ?, ?, ?)";
		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, books.getIsbn());
			ps.setString(2, books.getBookTitle());
			ps.setString(3, books.getAuthorName());
			ps.setString(4, books.getPublisherName());
			ps.setString(5, books.getDate());
			ps.setInt(6, books.getPrice());
			ps.setInt(7, books.getStock());
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert error:< " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return returnValue;
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println("connection Close error:< " + e.getMessage());
		}
	}

	public List<Books> select() {
		List<Books> list = new ArrayList<>();
		String selectQyery = "select * from booksTBL";
		try {
			ps = connection.prepareStatement(selectQyery);
			rs = ps.executeQuery();

			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}

			while (rs.next()) {
				String isbn = rs.getString("isbn");
				String bookTitle = rs.getString("title");
				String authorName = rs.getString("author");
				String publisherName = rs.getString("publisher");
				String date = rs.getString("publishedDate");
				int price = rs.getInt("price");
				int stock = rs.getInt("stock");

				list.add(new Books(isbn, bookTitle, authorName, publisherName, date, price, stock));
			}
		} catch (Exception e) {
			System.out.println("error : faild set list from DB :< " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("error : resource close :< " + e.getMessage());
			}
		}
		return list;
	}

	public int delete(String dleleteNumber) {
		int returnValue = -1;
		String deletQyery = "delete from booksTBL where ISBN = ? ";
		try {
			ps = connection.prepareStatement(deletQyery);
			ps.setString(1, dleleteNumber);
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("delete error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("resource close error " + e.getMessage());
			}
		}
		return returnValue;
	}

	public List<Books> search(String searchIsbn) {
		List<Books> list = new ArrayList<>();
		String searchQuery = "select * from booksTBL where ISBN = ? ";
		try {
			ps = connection.prepareStatement(searchQuery);
			ps.setString(1, searchIsbn);
			rs = ps.executeQuery();
			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}

			while (rs.next()) {
				String isbn = rs.getString("isbn");
				String bookTitle = rs.getString("title");
				String authorName = rs.getString("author");
				String publisherName = rs.getString("publisher");
				String date = rs.getString("publishedDate");
				int price = rs.getInt("price");
				int stock = rs.getInt("stock");

				list.add(new Books(isbn, bookTitle, authorName, publisherName, date, price, stock));
			}
		} catch (Exception e) {
			System.out.println("error : faild set list from DB :< " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("error : resource close :< " + e.getMessage());
			}
		}
		return list;

	}

	public int update(Books data, int soldCount) {
		int returnValue = -1;
		String updateQuery = "CALL proc_salesUpdate(?, ?)";
		try {
			ps = connection.prepareStatement(updateQuery);
			ps.setString(1, data.getIsbn());
			ps.setInt(2, soldCount);
			
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("error : faild update in DB :<" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("error : resource close :< " + e.getMessage());
			}
		}
		return returnValue;
	}


	public List<Books> selectOrderBy(int sortWay) {
		List<Books> list = new ArrayList<>();
		String orderByQuery = "select * from booksTBL order by stock ";
		try {
			switch (sortWay) {
			case 1:
				orderByQuery += "asc ";
				break;
			case 2:
				orderByQuery += "desc ";
				break;
			default:
				System.out.println("query error");
			}
			ps = connection.prepareStatement(orderByQuery);
			rs = ps.executeQuery();
			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
		while (rs.next()) {
				String isbn = rs.getString("isbn");
				String bookTitle = rs.getString("title");
				String authorName = rs.getString("author");
				String publisherName = rs.getString("publisher");
				String date = rs.getString("publishedDate");
				int price = rs.getInt("price");
				int stock = rs.getInt("stock");
				list.add(new Books(isbn, bookTitle, authorName, publisherName, date, price, stock));
			}
		} catch (Exception e) {
			System.out.println("sort error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("resource close error " + e.getMessage());
			}
		}
		return list;
	}


	
}
