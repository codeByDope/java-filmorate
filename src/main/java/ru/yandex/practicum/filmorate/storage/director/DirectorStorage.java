package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    List<Director> getAll();

    Optional<Director> getById(int id);

    Director add(Director director);

    Director update(Director director);

    void delete(int id);

    List<Director> getByIds(List<Integer> ids);

    List<Director> getAllFilmDirectors(Long filmId);
}