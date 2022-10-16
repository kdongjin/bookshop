package bookStore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class BookstoreExam {
	public static Scanner scanner = new Scanner(System.in);
	private static List<Books> list = new ArrayList<>();
	private static DBConnection dbCon = new DBConnection();
	private static String pattern = null;
	private boolean regex = false;

	public static void main(String[] args) {
		final int INPUT = 0, PRINT = 1, UPDATE = 2, DELETE = 3, SORT = 4, EXIT = 5;
		boolean stop = false;
		System.out.println("Please choose a number.");
		while (!stop) {
			switch (menu()) {
			case INPUT:
				inputData();
				break;
			case PRINT:
				printData();
				break;
			case UPDATE:
				updateData();
				break;
			case DELETE:
				deleteData();
				break;
			case SORT:
				sortData();
				break;
			case EXIT:
				System.out.println("data is stored safely. bye");
				break;
			default:
				System.out.println("Please. Choose from 0 to 5. ");
			}
		}

	}

	public static void inputData() {
		scanner.nextLine();

		try {
			System.out.println("Enter ISBN \n(000)-(00)-(00000)-(00)-(0)");
			String isbn = scanner.nextLine();
			boolean value = checkPattern(isbn, 1);
			if (!value) {
				return;
			}

			System.out.println("Enter the book title.");
			String title = scanner.nextLine();
			value = checkPattern(title, 2);
			if (!value) {
				return;
			}
			System.out.println("Enter the author.");
			String author = scanner.nextLine();
			value = checkPattern(author, 2);
			if (!value) {
				return;
			}

			System.out.println("Enter the publisher.");
			String publisher = scanner.nextLine();
			value = checkPattern(publisher, 2);
			if (!value) {
				return;
			}

			System.out.println("Enter the publication date.(yyyyMMdd)");
			String date = scanner.nextLine();
			String dateFormate = "yyyyMMdd";
			boolean checkDateType = dateCheck(date, dateFormate);
			if(!checkDateType) {
				System.out.println("Please enter according to the date type(yyyyMMdd)");
				return;
			}
			

			System.out.println("Enter the price.");
			int price = scanner.nextInt();
			value = checkPattern(String.valueOf(price), 3);
			if (!value) {
				return;
			}
			System.out.println("Enter the stock.");
			int stock = scanner.nextInt();
			value = checkPattern(String.valueOf(stock), 4);
			if (!value) {
				return;
			}

			Books books = new Books(isbn, title, author, publisher, date, price, stock);

			dbCon.connect();
			int result = dbCon.insert(books);
			if (result != -1) {
				System.out.println("Data entered successfully :)");
			} else {
				System.out.println("Failed to enter data :< ");
			}
			dbCon.close();
		} catch (InputMismatchException e) {
			System.out.println("error : type mismatch");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void printData() {
		try {
			dbCon.connect();
			list = dbCon.select();
			if (list.size() <= 0) {
				System.out.println("doesn't exist data");
				return;
			}
			for (Books books : list) {
				System.out.println(books);
			}
			dbCon.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void updateData() {
		List<Books> list = new ArrayList<>();
		scanner.nextLine();
		try {
			System.out.println("Enter the isbn number of the sold book.\n(000)-(00)-(00000)-(00)-(0)");
			String soldBook = scanner.nextLine();
			boolean value = checkPattern(soldBook, 1);
			if (!value) {
				return;
			}
			dbCon.connect();
			list = dbCon.search(soldBook);
			if (list.size() <= 0) {
				System.out.println("It doesn't exist.");
				return;
			}
			Books data = list.get(0);

			System.out.println("How many books did you sell?");
			int soldCount = scanner.nextInt();
			value = checkPattern(String.valueOf(soldCount), 4);
			if (!value) {
				return;
			}
			if (data.getStock() < soldCount || 0 > soldCount) {
				System.out.println("Please check sales quantity");
				return;
			}
			int updateValue = dbCon.update(data, soldCount);
			if (updateValue == -1) {
				System.out.println("update error");
				return;
			}
			list = dbCon.search(soldBook);
			System.out.println(list + "\nUpdate completed");
			dbCon.close();
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("update error " + e.getMessage());
		} finally {
			scanner.nextLine();
		}
	}

	public static void deleteData() {
		scanner.nextLine();
		try {
			System.out.println("Enter the ISBN number of books.\n(000)-(00)-(00000)-(00)-(0)");
			String dleleteNumber = scanner.nextLine();
			boolean value = checkPattern(dleleteNumber, 1);
			if (!value) {
				return;
			}

			dbCon.connect();
			int deleteData = dbCon.delete(dleleteNumber);
			if (deleteData != -1) {
				System.out.println("Delete completed :)");
			} else if (deleteData == 0) {
				System.out.println("It doesn't exist. ");
			} else {
				System.out.println("Delete Failed");
			}
			dbCon.close();
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			scanner.nextLine();
		}
	}

	private static void sortData() {
		scanner.nextLine();
		try {
			dbCon.connect();
			System.out.println("enter how to sort asc : 1, enter desc : 2 ");
			int sortWay = scanner.nextInt();
			boolean value = checkPattern(String.valueOf(sortWay), 5);
			if (!value) {
				return;
			}
			list = dbCon.selectOrderBy(sortWay);

			if (list.size() <= 0) {
				System.out.println("doesn't exist");
				return;
			}
			for (Books books : list) {
				System.out.println(books);
			}
			dbCon.close();
		} catch (Exception e) {
			System.out.println("database sort error " + e.getMessage());
		}
		return;
	}

	private static boolean checkPattern(String data, int type) {
		boolean regex = false;
		String message = null;
		switch (type) {
		case 1:
			pattern = "^[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9]$";
			message = "Please enter after confirm ISBN number.";
			break;
		case 2:
			pattern = "^[°¡-ÆR]{2,30}$";
			message = "Please enter after confirm name.";
			break;
		case 3:
			pattern = "^[0-9]{1,6}$";
			message = "Please enter after confirm number.";
			break;
		case 4:
			pattern = "^[0-9]{1,2}$";
			message = "Please enter after confirm price.";
			break;
		case 5:
			pattern = "^[1-2]$";
			message = "Please enter after confirm number.";
			break;
		}

		regex = Pattern.matches(pattern, data);
		if (!regex) {
			System.out.println(message);
			return false;
		}
		return regex;
	}
	
	public static boolean dateCheck(String date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static int menu() {
		System.out.println("=".repeat(83));
		System.out.println(" 0.enter data  | 1.show data | 2.updata data | 3.delete data | 4.sortData | 5.exit ");
		System.out.println("=".repeat(83));
		return scanner.nextInt();
	}
}
