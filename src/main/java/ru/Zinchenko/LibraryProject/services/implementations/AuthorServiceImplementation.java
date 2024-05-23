package ru.Zinchenko.LibraryProject.services.implementations;

import org.springframework.stereotype.Service;
import ru.Zinchenko.LibraryProject.models.Author;
import ru.Zinchenko.LibraryProject.models.Book;
import ru.Zinchenko.LibraryProject.models.repositaries.AuthorRepository;
import ru.Zinchenko.LibraryProject.services.interfaces.AuthorService;

import java.util.List;

@Service
public class AuthorServiceImplementation implements AuthorService {
    AuthorRepository repository;
    @Override
    public List<Author> findAll() {
        return repository.findAll();
    }

    @Override
    public Author findOne(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Author save(Author author) {
        return repository.save(author);
    }

    @Override
    public Author update(Author author) {
        if (!repository.existsById(author.getId())) {
            return null;
        }
        Author toUpdate = repository.findById(author.getId()).get();
        toUpdate.setName(author.getName());
        toUpdate.setSurname(author.getSurname());
        toUpdate.setMiddleName(author.getMiddleName());
        toUpdate.setBooks(author.getBooks());

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
