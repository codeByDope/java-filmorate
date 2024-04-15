package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class FeedController {


    @GetMapping("/{id}/feed")
    public List<Event> getFeed(@PathVariable Long id) {
        log.info("Был запрошен пользователь с ID");
//        return service.getUserById(id);
        return null;
    }
}
