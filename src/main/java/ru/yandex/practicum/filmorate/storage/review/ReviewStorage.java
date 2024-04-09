package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.lang.Nullable;
import ru.yandex.practicum.filmorate.exception.review.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;

import javax.validation.constraints.Null;
import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteReview(Review review);

    Optional<Review> getReviewById(Integer id);

    Collection<Review> getReviews(@Nullable Integer filmId, @Nullable Integer limit);

    default Review getReviewFromStorage(Integer reviewId) {
        return getReviewById(reviewId).orElseThrow(() -> new ReviewNotFoundException("Отзыв по id=" + reviewId + " не найден"));
    }

}
