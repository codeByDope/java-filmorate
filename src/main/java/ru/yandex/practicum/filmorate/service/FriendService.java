package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendService {
    private final FriendsStorage storage;
    private final UserService userService;

    public void add(Long userId, Long friendId) {
        log.info("Проверка существования пользователя {}", userId);
        userService.getUserById(userId);
        log.info("Проверка существования пользователя {}", friendId);
        userService.getUserById(friendId);
        storage.add(userId, friendId);
        log.info("Пользователь {} добавил в друзья пользователя {}", userId, friendId);
    }

    public void remove(Long userId, Long friendId) {
        log.info("Проверка существования пользователя {}", userId);
        userService.getUserById(userId);
        userService.getUserById(friendId);
        storage.remove(userId, friendId);
        log.info("Пользователь {} удалил из друзей {}", userId, friendId);
    }

    public List<User> getCommon(Long userId, Long friendId) {
        log.info("Проверка существования пользователя {}", userId);
        userService.getUserById(userId);
        userService.getUserById(friendId);
        log.info("Были запрошены общие друзья пользователей {} и {}", userId, friendId);
        return storage.getCommon(userId, friendId);
    }

    public List<User> getUsersFriends(Long id) {
        log.info("Проверка существования пользователя {}", id);
        userService.getUserById(id);
        return storage.getUserFriends(id);
    }
}
