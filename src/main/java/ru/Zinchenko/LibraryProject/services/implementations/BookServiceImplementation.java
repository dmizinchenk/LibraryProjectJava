package ru.Zinchenko.LibraryProject.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.Zinchenko.LibraryProject.models.Book;
import ru.Zinchenko.LibraryProject.models.repositaries.BookRepository;
import ru.Zinchenko.LibraryProject.services.interfaces.BookService;

import java.util.List;

@Service
public class BookServiceImplementation implements BookService {
    private final BookRepository repository;

    @Autowired
    public BookServiceImplementation(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    public Page<Book> findAll(int page) {
        return repository.findAll(PageRequest.of(page, 12));
    }

    @Override
    public List<Book> findBook(String search) {
        return repository.findBookByTitleLikeIgnoreCase("%" + search + "%");
    }

    @Override
    public List<Book> findByAuthor(String search){
        return repository.findBookByAuthorIgnoreCase(search);
    }

    @Override
    public Book findOne(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }

    @Override
    public Book update(Book book) {
        if (!repository.existsById(book.getId())) {
            return null;
        }
        Book toUpdate = repository.findById(book.getId()).get();
        toUpdate.copy(book);
        repository.save(toUpdate);
        return toUpdate;
    }

    @Override
    public boolean deleteById(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
