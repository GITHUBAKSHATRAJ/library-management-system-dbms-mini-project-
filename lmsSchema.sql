
CREATE TABLE books (
    bookId INT PRIMARY KEY,
    title VARCHAR(100),
    author VARCHAR(100),
    available BOOLEAN DEFAULT TRUE
);


CREATE TABLE borrowed_books (
    bookId INT,
    userId INT,
    borrow_date DATE,
    return_date DATE,
    PRIMARY KEY (bookId, userId, borrow_date),
    FOREIGN KEY (bookId) REFERENCES books(bookId)
);


CREATE TABLE users (
    userId INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);
