package ru.yandex.practicum.filmorate.exception.film;

public class FilmHasAlreadyCreatedException extends RuntimeException{
    public FilmHasAlreadyCreatedException(String msg) {
        super(msg);
    }
}
