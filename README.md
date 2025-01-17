# Library Management System

## Overview
The **Library Management System** is a Java-based application designed to streamline the management of books and users in a library. The system is connected to a MySQL database to store and retrieve information about books, users, and borrowing transactions.

### Key Features:
1. **Add New Books**: Allows administrators to add new books to the library's inventory.
2. **View All Books**: Displays all the books available in the library.
3. **Search Books by Title**: Enables users to search for books by their title.
4. **Borrow and Return Books**: Tracks borrowing and returning of books, including their availability status.
5. **View Borrowed Books**: Displays the list of books currently borrowed by a specific user.
6. **User Management**: Manages user details and borrowing history.

---

## Database Schema
The system uses a MySQL database with the following schema:

### `books` Table
```sql
CREATE TABLE books (
    bookId INT PRIMARY KEY,
    title VARCHAR(100),
    author VARCHAR(100),
    available BOOLEAN DEFAULT TRUE
);
```

### `borrowed_books` Table
```sql
CREATE TABLE borrowed_books (
    bookId INT,
    userId INT,
    borrow_date DATE,
    return_date DATE,
    PRIMARY KEY (bookId, userId, borrow_date),
    FOREIGN KEY (bookId) REFERENCES books(bookId)
);
```

### `users` Table
```sql
CREATE TABLE users (
    userId INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);
```

---

## How It Works

### 1. **Adding Books**
- **Input**: Book ID, Title, Author.
- **Output**: Confirmation of the book being added.

**Example**:
```
Enter Book ID: 101
Enter Book Title: Java Programming
Enter Author Name: James Gosling
Output: Rows affected: 1
```

### 2. **Viewing All Books**
- Displays all books in the library along with their availability.

**Example**:
```
Book ID | Title            | Author          | Available
---------------------------------------------------------
101     | Java Programming | James Gosling  | Yes
102     | Python Basics    | Guido van Rossum | No
```

### 3. **Searching Books by Title**
- **Input**: Partial or full title of the book.
- **Output**: List of matching books.

**Example**:
```
Enter Book Title to Search: Java
Output:
Book ID | Title            | Author
-----------------------------------
101     | Java Programming | James Gosling
```

### 4. **Borrowing Books**
- **Input**: Book ID, User ID.
- **Output**: Confirms if the book was borrowed successfully or if it’s unavailable.

**Example**:
```
Enter Book ID to Borrow: 101
Enter User ID: 1
Output: Rows affected: 1
```

### 5. **Returning Books**
- **Input**: Book ID, User ID.
- **Output**: Confirms if the book was returned successfully.

**Example**:
```
Enter Book ID to Return: 101
Enter User ID: 1
Output: Rows affected: 1
```

### 6. **Viewing Borrowed Books**
- **Input**: User ID.
- **Output**: Displays a list of books currently borrowed by the user.

**Example**:
```
Enter User ID to View Borrowed Books: 1
Output:
Book ID | Title            | Borrow Date
----------------------------------------
101     | Java Programming | 2025-01-15
```

---

## Visual Representation

### System Flow:
1. **Administrator Logs In**.
2. **Selects Action** from the menu (e.g., Add Book, Borrow Book, etc.).
3. **Input Data**: Administrator or user provides necessary details.
4. **Database Interaction**: Queries are executed on the MySQL database.
5. **Output Display**: Results are displayed to the administrator or user.

---

## Sample Outputs

### Adding a Book
**Command**:
```
Enter Book ID: 105
Enter Book Title: Advanced SQL
Enter Author Name: Chris Date
```
**Output**:
```
Rows affected: 1
```

### Borrowing a Book
**Command**:
```
Enter Book ID to Borrow: 105
Enter User ID: 2
```
**Output**:
```
Rows affected: 1
```

### Viewing All Books
**Command**:
```
View All Books
```
**Output**:
```
Book ID | Title            | Author          | Available
---------------------------------------------------------
101     | Java Programming | James Gosling   | No
102     | Python Basics    | Guido van Rossum| Yes
105     | Advanced SQL     | Chris Date      | No
```

---

## Prerequisites

1. **Java Development Kit (JDK)**: Ensure you have JDK 8 or higher installed.
2. **MySQL Server**: A running MySQL database server.
3. **JDBC Driver**: Ensure the JDBC driver for MySQL is included in your project.

---

## How to Run

1. Clone or download the project files.
2. Set up the MySQL database using the provided schema.
3. Update the database connection details in the `DBConnection` class.
4. Compile and run the Java program.

---

## Future Enhancements

1. **Overdue Notifications**: Notify users about overdue books and penalties.
2. **User Authentication**: Implement login functionality for users and admins.
3. **Book Categories**: Add support for categorizing books (e.g., Fiction, Non-fiction, Science).
4. **Reports**: Generate monthly or yearly reports for library activities.

---

Feel free to contribute to the project by suggesting or implementing new features!

