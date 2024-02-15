package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public ResponseEntity<?> getFilms() {
        log.info("Были запрошены фильмы. Их количество - {}", films.size());
        return ResponseEntity.ok(films.values());
    }

    @PostMapping
    public ResponseEntity<?> addFilm(@Valid @RequestBody Film film) {
        try {
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
                        return ResponseEntity.badRequest().body(film);
                    }
                }

            }
            log.info("Добавлен фильм с ID: {}", film.getId());
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(film);
        }
        return ResponseEntity.ok(film);
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film) {
        try {
            if (validateFilm(film)) {
                if (film.getId() == null) {
                    log.warn("Невозможно обновить фильм, не указав его айди");
                    return ResponseEntity.badRequest().body(film);
                } else {
                    if (films.containsKey(film.getId())) {
                        films.put(film.getId(), film);
                    } else {
                        log.warn("Невозможно обновить несуществующий фильм");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
                    }
                }
            }
            log.info("Обновлен фильм с ID: {}", film.getId());
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(film);
        }
        return ResponseEntity.ok(film);
    }

    private boolean validateFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Ошибка добавления фильма. Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException();
        }
        return true;
    }
}
