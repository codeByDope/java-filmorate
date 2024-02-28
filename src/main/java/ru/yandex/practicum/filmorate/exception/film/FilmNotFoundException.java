package ru.yandex.practicum.filmorate.exception.film;

public class FilmNotFoundException extends RuntimeException{
    public FilmNotFoundException(String msg) {
        super(msg);
    }
}
