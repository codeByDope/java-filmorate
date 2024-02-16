package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public ResponseEntity<Collection<User>> getUsers() {
        log.info("Запрошены пользователи");
        return ResponseEntity.ok(users.values());
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        try {
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
                        return ResponseEntity.badRequest().body(user);
                    }
                }
                log.info("Создан пользователь с id {}", user.getId());
            }
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(user);
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        try {
            if (validateUser(user)) {
                if (user.getId() == null) {
                    log.warn("Невозможно обновить пользователя, не указав его айди");
                    return ResponseEntity.badRequest().body(user);
                } else {
                    if (users.containsKey(user.getId())) {
                        users.put(user.getId(), user);
                    } else {
                        log.warn("Невозможно обновить несуществующего пользователя");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
                    }
                }
            }
            log.info("Пользователь с id {} обновлен", user.getId());
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(user);
        }
        return ResponseEntity.ok(user);
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
