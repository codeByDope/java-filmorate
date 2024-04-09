package ru.yandex.practicum.filmorate.service.films;

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
public class FilmServiceImpl implements FilmService {
    private final FilmStorage storage;

    @Override
    public List<Film> get() {
        return storage.getAll();
    }

    @Override
    public Film getById(Long id) {
        return storage.getById(id)
                .orElseThrow(() -> new FilmNotFoundException("Фильм с идентификатором " + id + " не найден"));
    }

    @Override
    public Film add(Film film) {
        Long id = film.getId();
        if (id != null && !storage.getById(id).isEmpty()) {
            throw new FilmHasAlreadyCreatedException("Фильм с таким ID уже существует!");
        } else if (id != null && id < 0) {
            throw new IllegalArgumentException("Фильм не может иметь отрицательный ID!");
        }
        return storage.add(film);
    }

    @Override
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

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID фильма не указан");
        } else if (id < 0) {
            throw new IllegalArgumentException("ID фильма не иожет быть отрицательным значением");
        } else if (storage.getById(id).isEmpty()) {
            throw new FilmNotFoundException("Фильм с указаным ID не найден");
        } else {
            storage.delete(id);
        }
    }
}
