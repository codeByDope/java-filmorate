package ru.yandex.practicum.filmorate.service.directors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.director.DirectorHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.director.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final DirectorStorage directorStorage;

    @Override
    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    @Override
    public Director getById(int id) {
        Director director = directorStorage.getById(id);
        if (director == null) {
            throw new DirectorNotFoundException("Режиссёра с таким id не существует");
        }
        return director;
    }

    @Override
    public Director add(Director director) {
        if (directorStorage.getAll().contains(director)) {
            throw new DirectorHasAlreadyCreatedException("Режиссёр уже существует");
        }
        return directorStorage.add(director);
    }

    @Override
    public Director update(Director director) {
        if (directorStorage.getById(director.getId()) == null) {
            throw new DirectorNotFoundException("Режиссёрa с таким id не существует");
        }
        return directorStorage.update(director);
    }

    @Override
    public void delete(int id) {
        if (directorStorage.getById(id) == null) {
            throw new DirectorNotFoundException("Режиссёра с таким id не существует");
        }
        directorStorage.delete(id);
    }
}