package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @Size(min = 0, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private long duration;
    @Getter
    private static int idCounter;

    public static int increaseIdCounter() {
        idCounter++;
        return idCounter;
    }
}
