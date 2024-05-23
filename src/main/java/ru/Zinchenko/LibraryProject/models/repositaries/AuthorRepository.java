package ru.Zinchenko.LibraryProject.models.repositaries;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.Zinchenko.LibraryProject.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
