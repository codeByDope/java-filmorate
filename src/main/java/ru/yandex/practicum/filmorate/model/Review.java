package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Builder(toBuilder = true)
@Value
public class Review {

    Long reviewId;
    Integer userId;
    Integer filmId;
    @NotBlank String content;
    boolean isPositive;
    int useful;
}