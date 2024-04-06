package ru.yandex.practicum.filmorate.service.ratings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.rating.RatingHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.rating.RatingNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.films.FilmService;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MpaRatingServiceImpl implements MpaRatingService {
    private final RatingStorage storage;
    private final FilmService filmService;

    @Override
    public MpaRating add(MpaRating rating) {
        Integer id = rating.getId();
        if (id != null && !storage.getById(id).isEmpty()) {
            throw new RatingHasAlreadyCreatedException("Рейтинг с таким ID уже существует!");
        } else if (id != null && id < 0) {
            throw new RatingHasAlreadyCreatedException("Рейтинг не может иметь отрицательный ID!");
        } else {
            return storage.add(rating);
        }
    }

    @Override
    public void delete(Integer id) {
        if (storage.getById(id).isEmpty()) {
            throw new RatingNotFoundException("Рейтинга с таким ID не существует!");
        } else {
            storage.delete(id);
        }
    }

    @Override
    public List<MpaRating> getAll() {
        return storage.getAll();
    }

    @Override
    public MpaRating getByFilmId(Long id) {
        filmService.getById(id);
        return storage.getByFilmId(id).orElse(null);
    }

    @Override
    public MpaRating getById(Integer id) {
        return storage.getById(id)
                .orElseThrow(() -> new RatingNotFoundException("Рейтинга с таким ID не существует!"));
    }
}
