package ru.yandex.practicum.filmorate.converter;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.domain.ReviewDO;
import ru.yandex.practicum.filmorate.model.Review;

@Component
public class ReviewDO2ReviewConverter {

    public Review convert(ReviewDO reviewDO) {
        return Review.builder()
                .reviewId(reviewDO.getReviewId())
                .userId(reviewDO.getUserId())
                .filmId(reviewDO.getFilmId())
                .content(reviewDO.getContent())
                .isPositive(reviewDO.isPositive())
                .useful(reviewDO.getLikesNumber() - reviewDO.getDislikesNumber())
                .build();
    }
}
