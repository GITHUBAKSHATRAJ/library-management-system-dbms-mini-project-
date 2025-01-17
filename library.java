import java.sql.*;
import java.util.Scanner;

class Main {

    DBConnection db = new DBConnection(); // Class for DB connection details

    int c = 0;

    Connection cn = null;
    Statement smt = null;
    ResultSet rs = null;
    PreparedStatement psmt = null;

    // Method to insert a book
    public int insertBook(int bookId, String title, String author) throws SQLException {
        try (Connection cn = DriverManager.getConnection(db.url, db.username, db.pwd)) {
            psmt = cn.prepareStatement("INSERT INTO books (bookId, title, author) VALUES (?, ?, ?)");
            psmt.setInt(1, bookId);
            psmt.setString(2, title);
            psmt.setString(3, author);
            c = psmt.executeUpdate();
        }
        return c;
    }

    // Method to display all books
    public void showAllBooks() throws SQLException {
        try (Connection cn = DriverManager.getConnection(db.url, db.username, db.pwd)) {
            smt = cn.createStatement();
            rs = smt.executeQuery("SELECT * FROM books");
            System.out.println("Book ID | Title | Author");
            while (rs.next()) {
                int bookId = rs.getInt("bookId");
                String title = rs.getString("title");
                String author = rs.getString("author");
                System.out.println(bookId + " | " + title + " | " + author);
            }
        }
    }

    // Method to search books by title
    public void searchBookByTitle(String title) throws SQLException {
        try (Connection cn = DriverManager.getConnection(db.url, db.username, db.pwd)) {
            psmt = cn.prepareStatement("SELECT * FROM books WHERE title LIKE ?");
            psmt.setString(1, "%" + title + "%");
            rs = psmt.executeQuery();
            System.out.println("Book ID | Title | Author");
            while (rs.next()) {
                int bookId = rs.getInt("bookId");
                String bookTitle = rs.getString("title");
                String author = rs.getString("author");
                System.out.println(bookId + " | " + bookTitle + " | " + author);
            }
        }
    }

    // Method to borrow a book (marks it as unavailable)
    public int borrowBook(int bookId, int userId) throws SQLException {
        int rows = 0;
        try (Connection cn = DriverManager.getConnection(db.url, db.username, db.pwd)) {
            // Check if the book is available
            psmt = cn.prepareStatement("SELECT available FROM books WHERE bookId = ?");
            psmt.setInt(1, bookId);
            rs = psmt.executeQuery();
            if (rs.next() && rs.getInt("available") == 1) {
                // Mark the book as borrowed
                psmt = cn.prepareStatement("UPDATE books SET available = 0 WHERE bookId = ?");
                psmt.setInt(1, bookId);
                rows = psmt.executeUpdate();
                // Record the borrowing transaction
                psmt = cn.prepareStatement(
                        "INSERT INTO borrowed_books (bookId, userId, borrow_date) VALUES (?, ?, NOW())");
                psmt.setInt(1, bookId);
                psmt.setInt(2, userId);
                psmt.executeUpdate();
            } else {
                System.out.println("Book is not available.");
            }
        }
        return rows;
    }

    // Method to return a book (marks it as available again)
    public int returnBook(int bookId, int userId) throws SQLException {
        int rows = 0;
        try (Connection cn = DriverManager.getConnection(db.url, db.username, db.pwd)) {
            // Check if the book is borrowed
            psmt = cn.prepareStatement(
                    "SELECT * FROM borrowed_books WHERE bookId = ? AND userId = ? AND return_date IS NULL");
            psmt.setInt(1, bookId);
            psmt.setInt(2, userId);
            rs = psmt.executeQuery();
            if (rs.next()) {
                // Mark the book as returned
                psmt = cn.prepareStatement("UPDATE books SET available = 1 WHERE bookId = ?");
                psmt.setInt(1, bookId);
                rows = psmt.executeUpdate();
                // Record the return date
                psmt = cn.prepareStatement(
                        "UPDATE borrowed_books SET return_date = NOW() WHERE bookId = ? AND userId = ?");
                psmt.setInt(1, bookId);
                psmt.setInt(2, userId);
                psmt.executeUpdate();
            } else {
                System.out.println("This book was not borrowed by the user.");
            }
        }
        return rows;
    }

    // Method to view borrowed books by a user
    public void viewBorrowedBooks(int userId) throws SQLException {
        try (Connection cn = DriverManager.getConnection(db.url, db.username, db.pwd)) {
            psmt = cn.prepareStatement("SELECT * FROM borrowed_books WHERE userId = ? AND return_date IS NULL");
            psmt.setInt(1, userId);
            rs = psmt.executeQuery();
            System.out.println("Book ID | Title | Borrow Date");
            while (rs.next()) {
                int bookId = rs.getInt("bookId");
                psmt = cn.prepareStatement("SELECT title FROM books WHERE bookId = ?");
                psmt.setInt(1, bookId);
                ResultSet bookRs = psmt.executeQuery();
                if (bookRs.next()) {
                    String title = bookRs.getString("title");
                    Date borrowDate = rs.getDate("borrow_date");
                    System.out.println(bookId + " | " + title + " | " + borrowDate);
                }
            }
        }
    }

    // Main method
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        Main library = new Main();
        int choice = 0;

        while (choice != 1) {
            System.out.println(
                    "1: Exit, 2: Add Book, 3: View All Books, 4: Search Book by Title, 5: Borrow Book, 6: Return Book, 7: View Borrowed Books");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.exit(0);
                case 2:
                    System.out.print("Enter Book ID: ");
                    int bookId = sc.nextInt();
                    sc.nextLine(); // Consume newline
                    System.out.print("Enter Book Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author Name: ");
                    String author = sc.nextLine();
                    System.out.println("Rows affected: " + library.insertBook(bookId, title, author));
                    break;
                case 3:
                    library.showAllBooks();
                    break;
                case 4:
                    sc.nextLine(); // Consume newline
                    System.out.print("Enter Book Title to Search: ");
                    String searchTitle = sc.nextLine();
                    library.searchBookByTitle(searchTitle);
                    break;
                case 5:
                    System.out.print("Enter Book ID to Borrow: ");
                    bookId = sc.nextInt();
                    System.out.print("Enter User ID: ");
                    int userId = sc.nextInt();
                    System.out.println("Rows affected: " + library.borrowBook(bookId, userId));
                    break;
                case 6:
                    System.out.print("Enter Book ID to Return: ");
                    bookId = sc.nextInt();
                    System.out.print("Enter User ID: ");
                    userId = sc.nextInt();
                    System.out.println("Rows affected: " + library.returnBook(bookId, userId));
                    break;
                case 7:
                    System.out.print("Enter User ID to View Borrowed Books: ");
                    userId = sc.nextInt();
                    library.viewBorrowedBooks(userId);
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
        sc.close();
    }
}
