package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.likers.LikerService;

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
}
