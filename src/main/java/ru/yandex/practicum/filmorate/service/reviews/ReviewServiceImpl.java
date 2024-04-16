package ru.yandex.practicum.filmorate.service.reviews;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FeedEventType;
import ru.yandex.practicum.filmorate.model.FeedOperationType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.feed.FeedService;
import ru.yandex.practicum.filmorate.service.films.FilmService;
import ru.yandex.practicum.filmorate.service.users.UserService;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;
    private final UserService userService;
    private final FilmService filmService;
    private final FeedService feedService;

    @Override
    public Review addReview(Review review) {
        Review reviewFromDb;
        userService.getUserById(review.getUserId());
        filmService.getById(review.getFilmId());
        reviewFromDb = reviewStorage.addReview(review);
        feedService.addEvent(review.getUserId(), reviewFromDb.getReviewId(), FeedEventType.REVIEW, FeedOperationType.ADD);
        return reviewFromDb;
    }

    @Override
    public Review updateReview(Review review) {
        Review reviewFromDb;
        userService.getUserById(review.getUserId());
        filmService.getById(review.getFilmId());
        reviewFromDb = reviewStorage.updateReview(review);
        feedService.addEvent(review.getUserId(), reviewFromDb.getReviewId(), FeedEventType.REVIEW, FeedOperationType.UPDATE);
        return reviewFromDb;
    }

    @Override
    public void deleteReview(int reviewId) {
        Optional<Review> review = reviewStorage.getReviewById((long) reviewId);
        review.ifPresent(value -> feedService.addEvent(value.getUserId(), value.getReviewId(), FeedEventType.REVIEW, FeedOperationType.REMOVE));
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
