package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.utils.ApiPathConstants;
import ru.yandex.practicum.filmorate.service.likers.LikerService;

@Slf4j
@RequestMapping(ApiPathConstants.FILM_PATH)
@RequiredArgsConstructor
@RestController
public class LikersController {
    private final LikerService service;

    @PutMapping(ApiPathConstants.LIKE_PATH)
    public Boolean addLike(@PathVariable Long id, @PathVariable Long userId) {
        service.add(id, userId);
        return true;
    }

    @DeleteMapping(ApiPathConstants.LIKE_PATH)
    public Boolean removeLike(@PathVariable Long id, @PathVariable Long userId) {
        service.delete(id, userId);
        return true;
    }
}
