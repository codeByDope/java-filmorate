package ru.yandex.practicum.filmorate.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ReviewDO {
    @NonNull Integer reviewId;
    @NonNull Integer userId;
    @NonNull Integer filmId;
    String content;
    boolean isPositive;
    int useful;
}
