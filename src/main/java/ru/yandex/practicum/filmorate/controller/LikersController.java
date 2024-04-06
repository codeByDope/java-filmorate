package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.LikerService;

import java.util.List;

@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
@RestController
public class LikersController {
    private final LikerService service;

    @PutMapping("/{id}/like/{userId}")
    public Boolean addLike(@PathVariable Long id, @PathVariable Long userId) {
        service.add(id, userId);
        return true;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Boolean removeLike(@PathVariable Long id, @PathVariable Long userId) {
        service.delete(id, userId);
        return true;
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return service.getMostPopularFilms(count);
    }
}
