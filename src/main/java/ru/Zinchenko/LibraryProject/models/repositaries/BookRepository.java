package ru.Zinchenko.LibraryProject.models.repositaries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.Zinchenko.LibraryProject.models.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBookByTitleLikeIgnoreCase(String title);
    @Query(value = "SELECT books.* " +
            "FROM books " +
            "LEFT JOIN authorsbooks ab on books.id = ab.bookid " +
            "LEFT JOIN authors on authors.id = ab.authorid " +
            "WHERE authors.surname ILIKE(CONCAT('%',:author,'%'))", nativeQuery = true)
    List<Book> findBookByAuthorIgnoreCase (@Param("author") String surname);
}
