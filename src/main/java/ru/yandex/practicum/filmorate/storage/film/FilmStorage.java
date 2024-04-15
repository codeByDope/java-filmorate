package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film add(Film film);

    Film update(Film film);

    void delete(Long id);

    List<Film> getAll();

    Optional<Film> getById(Long id);

    List<Film> getDirectorsFilmSortedByLikes(int directorId);

    List<Film> getDirectorsFilmSortedByYears(int directorId);

    Collection<Film> getPopular(Integer limit, Integer genreId, Integer year);

    List<Film> getMostPopularFilms(Long count);
}
