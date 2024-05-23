DROP TABLE IF EXISTS Authors;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Books;
DROP TABLE IF EXISTS Orders;

CREATE TABLE Users
(
    id SERIAL PRIMARY KEY,
    username VARCHAR (50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_CUSTOMER',
    isblocked BOOLEAN DEFAULT FALSE
);

CREATE TABLE Authors
(
    id SERIAL PRIMARY KEY,
    name VARCHAR (30),
    surname VARCHAR (50),
    middleName VARCHAR (50) NULL
);

CREATE TABLE Books
(
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    annotation text NULL,
    dateOfRent date DEFAULT NULL,
    userId int REFERENCES Users(id) ON DELETE SET NULL DEFAULT NULL
);

CREATE TABLE Orders
(
    id SERIAL PRIMARY KEY ,
    bookId int REFERENCES Books(id) NOT NULL,
    userId int REFERENCES Users(id) NOT NULL,
    isApproved bool NULL
);

CREATE TABLE AuthorsBooks
(
    id SERIAL PRIMARY KEY,
    bookId int REFERENCES Books(id) NOT NULL,
    authorId int REFERENCES Authors(id) NULL
)