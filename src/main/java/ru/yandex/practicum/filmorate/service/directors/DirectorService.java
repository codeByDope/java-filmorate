package ru.yandex.practicum.filmorate.service.directors;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {
    List<Director> getAll();

    Director getById(int id);

    Director add(Director director);

    Director update(Director director);

    void delete(int id);
}