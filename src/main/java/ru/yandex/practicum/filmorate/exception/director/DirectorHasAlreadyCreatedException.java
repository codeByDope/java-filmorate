package ru.yandex.practicum.filmorate.exception.director;

public class DirectorHasAlreadyCreatedException extends RuntimeException {
    public DirectorHasAlreadyCreatedException(String message) {
        super(message);
    }
}
