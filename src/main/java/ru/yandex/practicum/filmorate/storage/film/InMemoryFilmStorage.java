package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.film.FilmHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        log.info("Были запрошены фильмы. Их количество - {}", films.size());
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            log.info("Были запрошены фильм с ID - {}", id);
            return films.get(id);
        }
        throw new FilmNotFoundException(String.format("Фильм с ID %d не существует!", id));
    }

    @Override
    public Film addFilm(Film film) {
        if (validateFilm(film)) {
            if (film.getId() == null) {
                while (true) {
                    if (!films.containsKey(Film.increaseIdCounter())) break;
                }
                film.setId(Film.getIdCounter());
                films.put(film.getId(), film);
            } else {
                if (!films.containsKey(film.getId())) {
                    films.put(film.getId(), film);
                } else {
                    log.warn("Фильм с id {} уже существует", film.getId());
                    throw new FilmHasAlreadyCreatedException(String.format("Фильм с id %d  уже существует", film.getId()));
                }
            }

        }
        log.info("Добавлен фильм с ID: {}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (validateFilm(film)) {
            if (film.getId() == null) {
                log.warn("Невозможно обновить фильм, не указав его айди");
                throw new FilmNotFoundException("Невозможно обновить фильм, не указав его айди");
            } else {
                if (films.containsKey(film.getId())) {
                    films.put(film.getId(), film);
                } else {
                    log.warn("Невозможно обновить несуществующий фильм");
                    throw new FilmNotFoundException("Невозможно обновить несуществующий фильм!");
                }
            }
        }
        log.info("Обновлен фильм с ID: {}", film.getId());
        return film;
    }

    @Override
    public Film deleteFilm(Long id) {
        if (films.containsKey(id)) {
            Film film = films.get(id);
            films.remove(id);
            return film;
        }
        throw new FilmNotFoundException("Фильма с таким ID нет! Удаление невозможно!");
    }

    private boolean validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Ошибка добавления фильма. Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        }
        return true;
    }
}
