package ru.yandex.practicum.filmorate.service.reviews;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;

    @Override
    public Review addReview(Review review) {
        return reviewStorage.addReview(review);
    }

    @Override
    public Review updateReview(Review review) {
        return reviewStorage.updateReview(review);
    }

    @Override
    public void deleteReview(int reviewId) {
        reviewStorage.deleteReview(reviewId);
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewStorage.getReviewFromStorage(id);
    }

    @Override
    public Collection<Review> getReviews(Integer filmId, Integer limit) {
        return reviewStorage.getReviews(filmId, limit);
    }

    @Override
    public void addLike(Long id, Integer userId) {
        if (userStorage.getById(userId.longValue()).isPresent()) {
            if (reviewStorage.getReviewById(id).isPresent()) {
                reviewStorage.addLikeToReview(id, userId);
            }
        }
    }

    @Override
    public void addDislike(Long id, Integer userId) {
        if (userStorage.getById(userId.longValue()).isPresent()) {
            if (reviewStorage.getReviewById(id).isPresent()) {
                reviewStorage.addDislikeToReview(id, userId);
            }
        }
    }

    @Override
    public void removeLike(Integer id, Integer userId) {
        reviewStorage.removeLike(id, userId);
    }

    @Override
    public void removeDislike(Integer id, Integer userId) {
        reviewStorage.removeDislike(id, userId);
    }
}
