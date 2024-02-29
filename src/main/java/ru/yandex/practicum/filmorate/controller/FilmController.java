package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
@RestController
public class FilmController {
    private final FilmService service;

    @GetMapping
    public ResponseEntity<Collection<Film>> getFilms() {
        return ResponseEntity.ok(service.getFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFilmById(id));
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.ok(service.addFilm(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.ok(service.updateFilm(film));
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
