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
    is_blocked BOOLEAN DEFAULT FALSE
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
    dateOfRent date DEFAULT NULL
--     user_id int REFERENCES Users(id) ON DELETE SET NULL DEFAULT NULL
);

CREATE TABLE Orders
(
    id SERIAL PRIMARY KEY ,
    bookid int NOT NULL REFERENCES Books(id),
    userid int REFERENCES Users(id),
    is_handled bool NULL DEFAULT FALSE,
    have_owner bool DEFAULT TRUE
);

CREATE TABLE AuthorsBooks
(
    id SERIAL PRIMARY KEY,
    bookid int REFERENCES Books(id) NOT NULL,
    authorid int REFERENCES Authors(id) NULL
)