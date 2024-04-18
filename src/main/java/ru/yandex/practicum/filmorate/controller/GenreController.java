package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.utils.ApiPathConstants;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genres.GenreService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.GENRE_PATH)
@RestController
public class GenreController {
    private final GenreService service;

    @GetMapping
    public List<Genre> getAllGenres() {
        log.info("Запрошен список всех жанров");
        return service.getAll();
    }

    @GetMapping(ApiPathConstants.BY_ID_PATH)
    public Genre getTitleById(@PathVariable Integer id) {
        log.info("Запрошено название жанра с ID {}!", id);
        System.out.println();
        return service.getById(id);
    }
}
