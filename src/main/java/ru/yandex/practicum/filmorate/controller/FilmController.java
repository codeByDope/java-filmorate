package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.films.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
@RestController
public class FilmController {
    private final FilmService service;

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

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorsFilmSortedByLikes(@PathVariable int directorId, @RequestParam String sortBy) {
        return service.getDirectorFilms(directorId, sortBy);
    }

    @NotNull
    @Positive
    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable Long id) {
        log.info("Было запрошено удаление фильма с id " + id);
        service.delete(id);
    }
}