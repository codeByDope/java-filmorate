package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.UserHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public List<User> getUsers() {
        return storage.getAll();
    }

    public User getUserById(Long id) {
        return storage.getById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с идентификатором " + id + " не найден"));
    }

    public User addUser(User user) {
        Long id = user.getId();
        if (id != null && !storage.getById(id).isEmpty()) {
            throw new UserHasAlreadyCreatedException("Пользователь с таким ID уже существует!");
        } else if (id != null && id < 0) {
            throw new IllegalArgumentException("ID пользователя не может быть отрицательным!");
        } else {
            return storage.add(user);
        }
    }

    public User updateUser(User user) {
        Long id = user.getId();
        if (id == null) {
            throw new IllegalArgumentException("ID пользователя не указан!");
        } else if (id < 0) {
            throw new IllegalArgumentException("ID пользователя не может быть отрицательным!");
        } else if (storage.getById(id).isEmpty()) {
            throw new UserNotFoundException("Пользователь с указанным ID не найден!");
        } else {
            return storage.update(user);
        }
    }

}
