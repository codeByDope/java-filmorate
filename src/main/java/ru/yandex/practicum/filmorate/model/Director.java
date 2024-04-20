package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder(toBuilder = true)
public class Director {
    @NotNull
    @Positive
    private int id;

    @NotBlank
    private String name;

}