package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder(toBuilder = true)
public class Review {

    @Positive
    private Long reviewId;
    @NotNull
    private Long userId;
    @NotNull
    private Long filmId;
    @NotBlank
    private String content;
    @NotNull
    private Boolean isPositive;
    private int useful;
}