package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
@Builder(toBuilder = true)
public class Director {
    @NotNull
    @Positive
    int id;

    @NotBlank
    String name;

}