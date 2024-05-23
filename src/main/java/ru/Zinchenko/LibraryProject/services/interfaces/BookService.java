package ru.Zinchenko.LibraryProject.services.interfaces;

import org.springframework.data.domain.Page;
import ru.Zinchenko.LibraryProject.models.Book;

import java.util.List;

public interface BookService extends DAO<Book>{

    List<Book> findBook(String search);
    List<Book> findByAuthor(String search);
    Page<Book> findAll(int page);
}
