package ru.yandex.practicum.filmorate.service.likers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FeedEventType;
import ru.yandex.practicum.filmorate.model.FeedOperationType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.feed.FeedService;
import ru.yandex.practicum.filmorate.storage.likers.LikerStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikerServiceImpl implements LikerService {
    private final LikerStorage storage;
    private final FeedService feedService;

    @Override
    public void add(Long filmId, Long userId) {
        log.info("Пользователь {} поставил лайк фильму {}.", userId, filmId);
        storage.add(filmId, userId);
        feedService.addEvent(userId, filmId, FeedEventType.LIKE, FeedOperationType.ADD);
    }

    @Override
    public void delete(Long filmId, Long userId) {
        log.info("Пользователь {} удалил лайк фильму {}.", userId, filmId);
        storage.delete(filmId, userId);
        feedService.addEvent(userId, filmId, FeedEventType.LIKE, FeedOperationType.REMOVE);
    }

    @Override
    public List<User> getLikersByFilmId(Long id) {
        log.info("Запрошены пользователи, поставившие лайк фильму с id {}", id);
        return storage.getLikersByFilmId(id);
    }

    @Override
    public List<Film> getFilmsByLikerId(Long id) {
        log.info("Запрошены фильмы, лайкнутые пользователем с id {}", id);
        return storage.getFilmsByLikerId(id);
    }

    @Override
    public List<Film> getMostPopularFilms(Long count) {
        log.info("Запрошены {} наиболее популярных фильмов", count);
        return storage.getMostPopularFilms(count);
    }
}
