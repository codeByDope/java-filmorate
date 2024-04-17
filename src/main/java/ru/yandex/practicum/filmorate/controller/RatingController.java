package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.utils.ApiPathConstants;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.ratings.MpaRatingService;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.MPA_PATH)
@RestController
public class RatingController {
    private final MpaRatingService service;

    @GetMapping
    public List<MpaRating> getAll() {
        log.info("Был запрошен список всех рейтингов!");
        return service.getAll();
    }

    @GetMapping(ApiPathConstants.BY_ID_PATH)
    public MpaRating getById(@PathVariable Integer id) {
        log.info("Запрошено название рейтинга с ID {}!", id);
        return service.getById(id);
    }
}
