package ru.yandex.practicum.filmorate.exception.rating;

public class RatingHasAlreadyCreatedException extends RuntimeException {
    public RatingHasAlreadyCreatedException(String msg) {
        super(msg);
    }
}
