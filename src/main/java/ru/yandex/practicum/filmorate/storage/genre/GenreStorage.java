package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Genre add(Genre genre);
    void delete(Integer id);
    List<Genre> getAll();
    List<Genre> getByFilmId(Long id);
    Optional<Genre> getById(Integer id);
}
