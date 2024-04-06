package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MpaRating {
    private Integer id;
    private String name;

    public MpaRating(int id) {
        this.id = id;
    }

    public MpaRating(String name) {
        this.name = name;
    }
}
