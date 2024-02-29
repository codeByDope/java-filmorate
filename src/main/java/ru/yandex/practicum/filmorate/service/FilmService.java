package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;

    public Collection<Film> getFilms() {
        return storage.getFilms();
    }

    public Film getFilmById(Long id) {
        return storage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        return storage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }

    public void addLike(Long filmId, Long userId) {
        storage.getFilmById(filmId).addLiker(userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        if (filmId < 0) {
            throw new FilmNotFoundException("Фильм не может иметь отрицательный ID");
        }
        if (userId < 0) {
            throw new UserNotFoundException("Пользователь не может иметь отрицательный ID");
        }
        storage.getFilmById(filmId).removeLiker(userId);
        log.info("Пользователь {} снял лайк фильму {}", userId, filmId);
    }

    public Collection<Film> getMostPopularFilms(Long count) {
        return storage.getFilms().stream()
                .sorted((o1, o2) -> o2.getCountOfLikerId().compareTo(o1.getCountOfLikerId()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
