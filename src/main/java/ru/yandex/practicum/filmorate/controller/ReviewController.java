package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.utils.ApiPathConstants;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.reviews.ReviewService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(ApiPathConstants.REVIEW_PATH)
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review add(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping(ApiPathConstants.BY_ID_PATH)
    public void delete(@PathVariable(name = "id") Integer id) {
        reviewService.deleteReview(id);
    }

    @GetMapping(ApiPathConstants.BY_ID_PATH)
    public Review getReviewById(@PathVariable(name = "id") Long id) {
        return reviewService.getReviewById(id);

    }

    @GetMapping
    public Collection<Review> getReviews(@RequestParam(name = "filmId", required = false) Integer filmId,
                                         @RequestParam(name = "count", required = false) Integer count) {

        return reviewService.getReviews(filmId, count);
    }

    @PutMapping(ApiPathConstants.LIKE_PATH)
    public ResponseEntity<Review> addLikeToReview(@PathVariable(name = "id") Long id,
                                                  @PathVariable(name = "userId") Integer userId) {
        reviewService.addLike(id, userId);
        return ResponseEntity.status(200)
                .build();
    }

    @PutMapping(ApiPathConstants.DISLIKE_PATH)
    public ResponseEntity<Review> addDislikeToReview(@PathVariable(name = "id") Long id,
                                                     @PathVariable(name = "userId") Integer userId) {
        reviewService.addDislike(id, userId);
        return ResponseEntity.status(200)
                .build();
    }

    @DeleteMapping(ApiPathConstants.LIKE_PATH)
    public ResponseEntity<Review> removeLike(@PathVariable(name = "id") Integer id,
                                             @PathVariable(name = "userId") Integer userId) {
        reviewService.removeLike(id, userId);
        return ResponseEntity.status(200)
                .build();
    }

    @DeleteMapping(ApiPathConstants.DISLIKE_PATH)
    public ResponseEntity<Review> removeDislike(@PathVariable(name = "id") Integer id,
                                                @PathVariable(name = "userId") Integer userId) {
        reviewService.removeDislike(id, userId);
        return ResponseEntity.status(200)
                .build();
    }


}