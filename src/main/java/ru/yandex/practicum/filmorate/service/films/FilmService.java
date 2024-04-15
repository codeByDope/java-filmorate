package ru.yandex.practicum.filmorate.service.films;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> get();

    Film getById(Long id);

    Film add(Film film);

    Film update(Film film);

    List<Film> getDirectorFilms(int directorId, String sortBy);

    void delete(Long id);
}