package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

public interface RatingStorage {
    MpaRating add(MpaRating rating);

    void delete(Integer id);

    List<MpaRating> getAll();

    Optional<MpaRating> getByFilmId(Long id);

    Optional<MpaRating> getById(Integer id);
}
