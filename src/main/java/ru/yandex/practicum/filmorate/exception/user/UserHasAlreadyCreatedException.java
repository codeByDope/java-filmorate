package ru.yandex.practicum.filmorate.exception.user;

public class UserHasAlreadyCreatedException extends RuntimeException{
    public UserHasAlreadyCreatedException(String msg) {
        super(msg);
    }
}
