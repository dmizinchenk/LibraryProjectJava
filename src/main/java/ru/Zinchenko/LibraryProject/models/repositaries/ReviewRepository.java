package ru.Zinchenko.LibraryProject.models.repositaries;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.Zinchenko.LibraryProject.models.Review;
import ru.Zinchenko.LibraryProject.security.entity.User;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findReviewByUserAndBookId(User user, int bookId);
    List<Review> findReviewByUserAndFavorite(User user, boolean fav);
    List<Review> findReviewsByBookId(int bookId);
}
