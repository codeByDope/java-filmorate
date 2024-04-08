package ru.yandex.practicum.filmorate.exception.like;

public class LikeHasAlreadyCreatedException extends RuntimeException {
    public LikeHasAlreadyCreatedException(String msg) {
        super(msg);
    }
}
