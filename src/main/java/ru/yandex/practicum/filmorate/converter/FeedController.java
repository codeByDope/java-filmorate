package ru.yandex.practicum.filmorate.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.service.feed.FeedService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class FeedController {
    private final FeedService service;

    @GetMapping("/{id}/feed")
    public List<Feed> getAll(@PathVariable Long id) {
        log.info("Запрос ленты событий для пользователя с Id {}", id);
        return service.getAllByUserId(id);
    }
}
