package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.utils.ApiPathConstants;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.films.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.FILM_PATH)
@RestController
@Validated
public class FilmController {
    private final FilmService service;

    @GetMapping
    public List<Film> get() {
        log.info("Был запрошен список всех фильмов.");
        return service.get();
    }

    @GetMapping(ApiPathConstants.BY_ID_PATH)
    public Film getFilmById(@PathVariable Long id) {
        log.info("Был запрошен фильм с id " + id);
        System.out.println(service.getById(id));
        return service.getById(id);
    }

    @GetMapping(ApiPathConstants.SEARCH_FILMS_PATH)
    public List<Film> search(@RequestParam String query,
                             @RequestParam(name = "by") List<String> filters) {
        log.info("Был запрошен поиск фильмов");
        return service.search(query, filters);
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

    @DeleteMapping(ApiPathConstants.BY_ID_PATH)
    public void deleteFilmById(@PathVariable @Positive Long id) {
        log.info("Было запрошено удаление фильма с id " + id);
        service.delete(id);
    }

    @GetMapping(ApiPathConstants.FILM_BY_DIRECTOR_PATH)
    public List<Film> getDirectorsFilmSortedByLikes(@PathVariable int directorId, @RequestParam String sortBy) {
        return service.getDirectorFilms(directorId, sortBy);
    }

    @GetMapping(ApiPathConstants.POPULAR_FILMS_PATH)
    public List<Film> getMostPopularFilms(@RequestParam(name = "count", required = false) String count,
                                          @RequestParam(name = "genreId", required = false) Integer genreId,
                                          @RequestParam(name = "year", required = false) Integer year) {
        Long countValue = Optional.ofNullable(count)
                .map(Long::parseLong)
                .orElse(10L);
        return service.getMostPopularFilms(countValue, genreId, year);
    }

    @GetMapping(ApiPathConstants.COMMON_PATH)
    public List<Film> getCommon(@RequestParam Long userId, @RequestParam Long friendId) {
        log.info("Были запрошены общие фильмы пользователей {} и {}", userId, friendId);
        return service.getCommon(userId, friendId);
    }
}