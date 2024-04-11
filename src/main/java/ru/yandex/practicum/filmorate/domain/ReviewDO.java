package ru.yandex.practicum.filmorate.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ReviewDO {
    @NonNull Long reviewId;
    @NonNull Long userId;
    @NonNull Long filmId;
    String content;
    boolean isPositive;
    int useful;
}
