package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    UserService service;
    InMemoryUserStorage storage;

    @Autowired
    public UserController(UserService service, InMemoryUserStorage storage) {
        this.service = service;
        this.storage = storage;
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getUsers() {
        return ResponseEntity.ok(storage.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(storage.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(storage.addUser(user));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(storage.updateUser(user));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.addFriend(id, friendId);
        return ResponseEntity.ok("Добавление друга прошло успешно.");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        System.out.println("УДАЛЯЕМ ДРУГА");
        service.removeFriend(id, friendId);
        return ResponseEntity.ok("Удаление друга прошло успешно");
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Collection<User>> getUsersFriends(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUsersFriends(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Collection<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return ResponseEntity.ok(service.getCommonFriends(id, otherId));
    }


}
