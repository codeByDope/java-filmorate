package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.genre.GenreHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage storage;
    private final FilmService filmService;

    public Genre add(Genre genre) {
        Integer id = genre.getId();
        if (id != null && !storage.getById(id).isEmpty()) {
            throw new GenreHasAlreadyCreatedException("Жанр с таким ID уже существует!");
        } else if (id != null && id < 0) {
            throw new IllegalArgumentException("Жанр не может иметь отрицательный ID!");
        } else {
            return storage.add(genre);
        }
    }

    public void delete(Integer id) {
        if (storage.getById(id).isEmpty()) {
            throw new GenreNotFoundException("Жанра с таким ID не существует!");
        } else {
            storage.delete(id);
        }
    }

    public List<Genre> getAll() {
        return storage.getAll();
    }

    public List<Genre> getByFilmId(Long id) {
        filmService.getById(id); // в случае, когда такого фильма нет - выбрасывается исключение
        return storage.getByFilmId(id);
    }

    public Genre getById(Integer id) {
        return storage.getById(id)
                .orElseThrow(() -> new GenreNotFoundException("Жанра с таким ID не существует!"));
    }
}
