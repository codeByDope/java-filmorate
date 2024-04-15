package ru.yandex.practicum.filmorate.exception.review;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(String msg) {
        super(msg);
    }

}
