package ru.Zinchenko.LibraryProject.models.repositaries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Zinchenko.LibraryProject.models.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
