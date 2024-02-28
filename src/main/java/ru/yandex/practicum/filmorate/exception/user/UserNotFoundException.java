package ru.yandex.practicum.filmorate.exception.user;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
