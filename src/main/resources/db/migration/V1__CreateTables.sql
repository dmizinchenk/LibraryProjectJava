DROP TABLE IF EXISTS Authors;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Books;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS AuthorsBooks;
DROP TABLE IF EXISTS Review;

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
    annotation TEXT NULL,
    dateOfRent DATE DEFAULT NULL,
    books_count INT DEFAULT floor(random()*5)+1 NOT NULL,
    pathToFile TEXT NULL
);

CREATE TABLE Orders
(
    id SERIAL PRIMARY KEY ,
    bookid INT NOT NULL REFERENCES Books(id),
    userid INT REFERENCES Users(id),
    state VARCHAR (25),
    reserved_by INT REFERENCES Users(id),
    returned_by INT REFERENCES Users(id)
);

CREATE TABLE AuthorsBooks
(
    id SERIAL PRIMARY KEY,
    bookid INT REFERENCES Books(id) NOT NULL,
    authorid INT REFERENCES Authors(id) NULL
);

CREATE TABlE Review
(
    id SERIAL PRIMARY KEY,
    userid INT NOT NULL REFERENCES Users(id) ON DELETE RESTRICT,
    bookid INT NOT NULL REFERENCES Books(id) ON DELETE RESTRICT,
    is_favorite BOOLEAN DEFAULT FALSE,
    comment TEXT,
    mark INT,
    CONSTRAINT uniq_key UNIQUE (userid, bookid)
)
