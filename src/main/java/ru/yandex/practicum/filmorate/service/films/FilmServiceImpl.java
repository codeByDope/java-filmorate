package ru.yandex.practicum.filmorate.service.films;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.directors.DirectorService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.ValidationException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage storage;
    private final DirectorService directorService;

    @Override
    public List<Film> get() {
        return storage.getAll();
    }

    @Override
    public Film getById(Long id) {
        return storage.getById(id)
                .orElseThrow(() -> new FilmNotFoundException("Фильм с идентификатором " + id + " не найден"));
    }

    @Override
    public Film add(Film film) {
        Long id = film.getId();
        if (id != null && storage.getById(id).isPresent()) {
            throw new FilmHasAlreadyCreatedException("Фильм с таким ID уже существует!");
        } else if (id != null && id < 0) {
            throw new IllegalArgumentException("Фильм не может иметь отрицательный ID!");
        }
        checkDirectors(film);
        return storage.add(film);
    }

    @Override
    public Film update(Film film) {
        Long id = film.getId();
        if (id == null) {
            throw new IllegalArgumentException("ID фильма не указан!");
        } else if (id < 0) {
            throw new IllegalArgumentException("ID фильма не может быть отрицательным!");
        } else if (storage.getById(id).isEmpty()) {
            throw new FilmNotFoundException("Фильм с указанным ID не найден!");
        } else {
            checkDirectors(film);
            return storage.update(film);
        }
    }

    @Override
    public List<Film> search(String query, List<String> filters) {
        if (filters.isEmpty() || !(filters.contains("title") || filters.contains("director")) || filters.size() > 2) {
            throw new ValidationException("Фильтр поиска задан некорректно: количество параметров: " +
                    filters.size() + " - Должен быть 1 или 2; В запросе присутствуют фильтры: " + filters +
                    " должны быть фильтры title, director");
        }
        return storage.search(query, filters);
    }

    @Override
    public List<Film> getDirectorFilms(int directorId, String sortBy) {
        directorService.getById(directorId);
        if (sortBy.equals("likes")) {
            return storage.getDirectorsFilmSortedByLikes(directorId);
        } else {
            return storage.getDirectorsFilmSortedByYears(directorId);
        }
    }

    public void delete(Long id) {
        if (storage.getById(id).isEmpty()) {
            throw new FilmNotFoundException("Фильм с указаным ID не найден");
        } else {
            storage.delete(id);
        }
    }

    private void checkDirectors(Film film) {
        if (film.getDirectors() != null) {
            List<Integer> ids = film.getDirectors().stream()
                    .map(Director::getId).collect(Collectors.toList());
            List<Director> directors = directorService.getByIds(ids);
            if (ids.size() != directors.size()) {
                throw new ValidationException("Неправильный режиссёр");
            }
            film.setDirectors(directors.stream()
                    .sorted(Comparator.comparing(Director::getId))
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public List<Film> getMostPopularFilms(Long count,
                                          @Nullable Integer genreId,
                                          @Nullable Integer year) {
        log.info("Запрошены {} наиболее популярных фильмов", count);
        return storage.getMostPopularFilms(count, genreId, year);
    }


    public List<Film> getCommon(Long userId, Long friendId) {
        return storage.getCommon(userId, friendId);
    }
}
