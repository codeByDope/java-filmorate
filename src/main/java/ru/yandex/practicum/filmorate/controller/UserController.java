package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.utils.ApiPathConstants;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.users.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.USER_PATH)
@RestController
@Validated
public class UserController {
    private final UserService service;

    @GetMapping
    public List<User> getUsers() {
        log.info("Были запрошены все пользователи!");
        return service.getUsers();
    }

    @GetMapping(ApiPathConstants.BY_ID_PATH)
    public User getUserById(@PathVariable Long id) {
        log.info("Был запрошен пользователь с ID");
        return service.getUserById(id);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Создан пользователь " + user.getName());
        return service.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return service.updateUser(user);
    }

    @DeleteMapping(ApiPathConstants.BY_ID_PATH)
    public void deleteUserById(@PathVariable @Positive Long id) {
        log.info("Было запрошено удаление пользователя с id " + id);
        service.delete(id);
    }

    @GetMapping(ApiPathConstants.RECOMMENDATIONS_PATH)
    public List<Film> getRecommendations(@PathVariable Long id) {
        log.info("Запрошены рекомендации для пользователя {}", id);
        return service.getRecommendations(id);
    }
}
