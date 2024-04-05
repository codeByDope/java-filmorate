package ru.yandex.practicum.filmorate.exception.genre;

public class GenreHasAlreadyCreatedException extends RuntimeException {
    public GenreHasAlreadyCreatedException(String msg) {
        super(msg);
    }
}
