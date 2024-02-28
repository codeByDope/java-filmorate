package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFriendException;
import ru.yandex.practicum.filmorate.exception.NotLikeException;
import ru.yandex.practicum.filmorate.exception.film.FilmHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;

import javax.validation.ValidationException;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        return Map.of("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler({FilmNotFoundException.class, FilmHasAlreadyCreatedException.class, NotLikeException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleFilmExceptions(final RuntimeException e) {
        return Map.of("error", "Ошибка действия с фильмом",
                "errorMessage", e.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, UserHasAlreadyCreatedException.class, NotFriendException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserExceptions(final RuntimeException e) {
        return Map.of("error", "Ошибка действия с пользователем",
                "errorMessage", e.getMessage());
    }


}
