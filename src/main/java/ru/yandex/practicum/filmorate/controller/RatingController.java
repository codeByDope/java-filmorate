package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mpa")
@RestController
public class RatingController {
    private final MpaRatingService service;

    @GetMapping
    public List<MpaRating> getAll() {
        log.info("Был запрошен список всех рейтингов!");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public MpaRating getById(@PathVariable Integer id) {
        log.info("Запрошено название рейтинга с ID {}!", id);
        return service.getById(id);
    }
}
