package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Builder(toBuilder = true)
@Value
public class Review {

    @Positive
    Long reviewId;
    @NotNull
    Long userId;
    @NotNull
    Long filmId;
    @NotBlank String content;
    @NotNull Boolean isPositive;
    int useful;
}