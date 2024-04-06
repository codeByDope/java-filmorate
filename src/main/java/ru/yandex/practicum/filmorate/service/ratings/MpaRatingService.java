package ru.yandex.practicum.filmorate.service.ratings;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaRatingService {
    MpaRating add(MpaRating rating);

    void delete(Integer id);

    List<MpaRating> getAll();

    MpaRating getByFilmId(Long id);

    MpaRating getById(Integer id);
}
