package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.director.DirectorHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.director.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.friend.AlreadyFriendsException;
import ru.yandex.practicum.filmorate.exception.friend.NotFriendException;
import ru.yandex.practicum.filmorate.exception.genre.GenreHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.like.LikeHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.like.NotLikeException;
import ru.yandex.practicum.filmorate.exception.rating.RatingHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.rating.RatingNotFoundException;
import ru.yandex.practicum.filmorate.exception.review.ReviewNotFoundException;
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

    @ExceptionHandler({FilmNotFoundException.class, FilmHasAlreadyCreatedException.class,
            NotLikeException.class, LikeHasAlreadyCreatedException.class,
            DirectorNotFoundException.class, DirectorHasAlreadyCreatedException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleFilmExceptions(final RuntimeException e) {
        return Map.of("error", "Ошибка действия с фильмом",
                "errorMessage", e.getMessage());
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleReviewNotFoundExceptions(final ReviewNotFoundException exc) {
        return Map.of("error", "Ошибка действия с ревью",
                "errorMessage", exc.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, UserHasAlreadyCreatedException.class, NotFriendException.class, AlreadyFriendsException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserExceptions(final RuntimeException e) {
        return Map.of("error", "Ошибка действия с пользователем",
                "errorMessage", e.getMessage());
    }

    @ExceptionHandler({RatingHasAlreadyCreatedException.class, RatingNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleRatingExceptions(final RuntimeException e) {
        return Map.of("error", "Ошибка действия с рейтингом",
                "errorMessage", e.getMessage());
    }

    @ExceptionHandler({GenreHasAlreadyCreatedException.class, GenreNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleGenreExceptions(final RuntimeException e) {
        return Map.of("error", "Ошибка действия с жанром",
                "errorMessage", e.getMessage());
    }

}
