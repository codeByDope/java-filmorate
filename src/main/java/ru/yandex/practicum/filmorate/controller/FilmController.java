package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.films.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
@RestController
public class FilmController {
    private final FilmService service;

    private static final String POPULAR_FILMS_PATH = "/popular";

    @GetMapping
    public List<Film> get() {
        log.info("Был запрошен список всех фильмов.");
        return service.get();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Был запрошен фильм с id " + id);
        System.out.println(service.getById(id));
        return service.getById(id);
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("Запрошено добавление фильма " + film.getName());
        return service.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Запрошено обновление фильма " + film.getId());
        return service.update(film);
    }

    @GetMapping(value = POPULAR_FILMS_PATH, params = {"count"})
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return service.getMostPopularFilms(count);
    }

    @GetMapping(value = POPULAR_FILMS_PATH, params = { "count", "genreId", "year"})
    public Collection<Film> getPopularByYearAndGenre(@RequestParam(name = "count") Integer count,
                                                     @RequestParam(name = "genreId") Integer genreId,
                                                     @RequestParam(name = "year") Integer year) {
        log.info(String.format("Запрошен список из %s в жанре %s за %s год", count, genreId, year));
        return service.getPopular(count, genreId, year);
    }


}
