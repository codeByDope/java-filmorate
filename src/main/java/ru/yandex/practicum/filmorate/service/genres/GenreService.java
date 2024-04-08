package ru.yandex.practicum.filmorate.service.genres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {
    Genre add(Genre genre);

    void delete(Integer id);

    List<Genre> getAll();

    List<Genre> getByFilmId(Long id);

    Genre getById(Integer id);
}
