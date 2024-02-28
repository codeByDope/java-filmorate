package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    FilmStorage storage;
    FilmService service;

    @Autowired
    public FilmController(FilmStorage storage, FilmService service) {
        this.storage = storage;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getFilms() {
        return ResponseEntity.ok(storage.getFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
        return ResponseEntity.ok(storage.getFilmById(id));
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.ok(storage.addFilm(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.ok(storage.updateFilm(film));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<String> addLike(@PathVariable Long id, @PathVariable Long userId) {
        service.addLike(id, userId);
        return ResponseEntity.ok("Бро(Сис), всё кул. Лайк поставлен!");
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> removeLike(@PathVariable Long id, @PathVariable Long userId) {
        service.removeLike(id, userId);
        return ResponseEntity.ok("Лайк убран!");
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> getMostPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return ResponseEntity.ok(service.getMostPopularFilms(count));
    }
}
