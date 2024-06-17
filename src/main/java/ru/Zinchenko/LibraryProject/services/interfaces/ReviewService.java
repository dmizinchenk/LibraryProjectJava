package ru.Zinchenko.LibraryProject.services.interfaces;

import ru.Zinchenko.LibraryProject.models.Review;
import ru.Zinchenko.LibraryProject.security.entity.User;

import java.util.List;
import java.util.Optional;

public interface ReviewService extends DAO<Review> {
    Optional<Review> getReviewByUserAndBookId(User user, int bookId);
    List<Review> getFavorites(User user);
    List<Integer> getMarks(int bookId);
}
