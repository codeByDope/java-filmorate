package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.lang.Nullable;
import ru.yandex.practicum.filmorate.exception.review.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteReview(int reviewId);

    Optional<Review> getReviewById(Integer id);

    Collection<Review> getReviews(@Nullable Integer filmId, @Nullable Integer limit);

    default Review getReviewFromStorage(Integer reviewId) {
        return getReviewById(reviewId).orElseThrow(() -> new ReviewNotFoundException("Отзыв по id=" + reviewId + " не найден"));
    }

    void addLikeToReview(Integer reviewId, Integer userId);

    void addDislikeToReview(Integer reviewId, Integer userId);

    void removeLike(Integer reviewId, Integer userId);

    void removeDislike(Integer reviewId, Integer userId);

}
