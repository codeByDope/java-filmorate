package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.user.UserHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        log.info("Запрошены пользователи");
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            log.info("Был запрошен пользователь с ID - {}", id);
            return users.get(id);
        }
        throw new UserNotFoundException(String.format("Пользователя с ID %d не существует", id));
    }

    @Override
    public User addUser(User user) {
        if (validateUser(user)) {
            if (user.getId() == null) {
                while (true) {
                    if (!users.containsKey(User.increaseIdCounter())) break;
                }
                user.setId(User.getIdCounter());
                users.put(user.getId(), user);
            } else {
                if (!users.containsKey(user.getId())) {
                    users.put(user.getId(), user);
                } else {
                    log.warn("Пользователь с id {} уже существует", user.getId());
                    throw new UserHasAlreadyCreatedException(String.format("Пользователь с id %d уже существует", user.getId()));
                }
            }
            log.info("Создан пользователь с id {}", user.getId());
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (validateUser(user)) {
            if (user.getId() == null) {
                log.warn("Невозможно обновить пользователя, не указав его айди");
                throw new UserNotFoundException("Невозможно обновить пользователя, не указав его айди");
            } else {
                if (users.containsKey(user.getId())) {
                    users.put(user.getId(), user);
                } else {
                    log.warn("Невозможно обновить несуществующего пользователя");
                    throw new UserNotFoundException("Невозможно обновить несуществующего пользователя");
                }
            }
        }
        log.info("Пользователь с id {} обновлен", user.getId());
        return user;
    }

    @Override
    public User deleteUser(Integer id) {
        if (users.containsKey(id)) {
            log.info("Был удален пользователь с ID {}", id);
            return users.get(id);
        }
        throw new UserNotFoundException(String.format("Пользователя с ID %d не существует. Удаление невозможно!", id));
    }

    private boolean validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин не должен содержать пробелы");
            throw new ValidationException();
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return true;
    }
}
