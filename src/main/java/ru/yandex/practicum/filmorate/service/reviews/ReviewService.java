package ru.yandex.practicum.filmorate.service.reviews;

import org.springframework.lang.Nullable;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewService {

    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteReview(int reviewId);

    Review getReviewById(Long id);

    Collection<Review> getReviews(@Nullable Integer filmId, @Nullable Integer limit);

    void addLike(Long id, Integer userId);

    void addDislike(Long id, Integer userId);

    void removeLike(Integer id, Integer userId);

    void removeDislike(Integer id, Integer userId);

}
