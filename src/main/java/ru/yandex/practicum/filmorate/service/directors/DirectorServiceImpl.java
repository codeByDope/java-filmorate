package ru.yandex.practicum.filmorate.service.directors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.director.DirectorHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.director.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;
import java.util.Set;

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
        return directorStorage.getById(id)
                .orElseThrow(() -> new DirectorNotFoundException("Режиссёрa с таким id не существует"));
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
        directorStorage.getById(director.getId())
                .orElseThrow(() -> new DirectorNotFoundException("Режиссёрa с таким id не существует"));
        return directorStorage.update(director);
    }

    @Override
    public void delete(int id) {
        directorStorage.getById(id)
                .orElseThrow(() -> new DirectorNotFoundException("Режиссёрa с таким id не существует"));
        directorStorage.delete(id);
    }

    @Override
    public Set<Director> getByIds(List<Integer> ids) {
        return directorStorage.getByIds(ids);
    }
}