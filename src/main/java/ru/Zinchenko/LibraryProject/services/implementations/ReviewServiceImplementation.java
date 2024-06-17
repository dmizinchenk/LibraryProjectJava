package ru.Zinchenko.LibraryProject.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.Zinchenko.LibraryProject.models.Review;
import ru.Zinchenko.LibraryProject.models.repositaries.ReviewRepository;
import ru.Zinchenko.LibraryProject.security.entity.User;
import ru.Zinchenko.LibraryProject.services.interfaces.ReviewService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImplementation implements ReviewService {
    private final ReviewRepository repository;
    @Override
    public List<Review> findAll() {
        return repository.findAll();
    }

    @Override
    public Review findOne(int id) {
        return repository.findById(id).get();
    }

    @Override
    public Review save(Review review) {
        return repository.save(review);
    }

    @Override
    public Review update(Review review) {
        if (!repository.existsById(review.getId())) {
            return null;
        }
        Review toUpdate = repository.findById(review.getId()).get();
        toUpdate.copy(review);
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

    @Override
    public Optional<Review> getReviewByUserAndBookId(User user, int bookId) {
        return repository.findReviewByUserAndBookId(user, bookId);
    }

    @Override
    public List<Review> getFavorites(User user) {
        return repository.findReviewByUserAndFavorite(user, true);
    }

    @Override
    public List<Integer> getMarks(int bookId) {
        List<Review> reviews = repository.findReviewsByBookId(bookId);
        return reviews.stream().map(Review::getMark).collect(Collectors.toList());
    }
}
