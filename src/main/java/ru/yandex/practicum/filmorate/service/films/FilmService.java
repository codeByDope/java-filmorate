package ru.yandex.practicum.filmorate.service.films;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    public List<Film> get();

    public Film getById(Long id);

    public Film add(Film film);

    public Film update(Film film);

    List<Film> getDirectorFilms(int directorId, String sortBy);

    void delete(Long id);
}