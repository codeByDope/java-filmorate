package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;

    public List<Film> get() {
        return storage.getAll();
    }

    public Film getById(Long id) {
        return storage.getById(id)
                .orElseThrow(() -> new FilmNotFoundException("Фильм с идентификатором " + id + " не найден"));
    }

    public Film add(Film film) {
        Long id = film.getId();
        if (id != null && !storage.getById(id).isEmpty()) {
            throw new FilmHasAlreadyCreatedException("Фильм с таким ID уже существует!");
        } else if (id != null && id < 0) {
            throw new IllegalArgumentException("Фильм не может иметь отрицательный ID!");
        }
        return storage.add(film);
    }

    public Film update(Film film) {
        Long id = film.getId();
        if (id == null) {
            throw new IllegalArgumentException("ID фильма не указан!");
        } else if (id < 0) {
            throw new IllegalArgumentException("ID фильма не может быть отрицательным!");
        } else if (storage.getById(id).isEmpty()) {
            throw new FilmNotFoundException("Фильм с указанным ID не найден!");
        } else {
            return storage.update(film);
        }
    }
}
