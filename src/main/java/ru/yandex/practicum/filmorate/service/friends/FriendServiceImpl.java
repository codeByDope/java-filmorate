package ru.yandex.practicum.filmorate.service.friends;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FeedEventType;
import ru.yandex.practicum.filmorate.model.FeedOperationType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.feed.FeedService;
import ru.yandex.practicum.filmorate.service.users.UserService;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendsStorage storage;
    private final UserService userService;
    private final FeedService feedService;

    @Override
    public void add(Long userId, Long friendId) {
        log.info("Проверка существования пользователя {}", userId);
        userService.getUserById(userId);
        log.info("Проверка существования пользователя {}", friendId);
        userService.getUserById(friendId);
        storage.add(userId, friendId);
        feedService.addEvent(userId, friendId, FeedEventType.FRIEND, FeedOperationType.ADD);
        log.info("Пользователь {} добавил в друзья пользователя {}", userId, friendId);
    }

    @Override
    public void remove(Long userId, Long friendId) {
        log.info("Проверка существования пользователя {}", userId);
        userService.getUserById(userId);
        userService.getUserById(friendId);
        storage.remove(userId, friendId);
        feedService.addEvent(userId, friendId, FeedEventType.FRIEND, FeedOperationType.REMOVE);
        log.info("Пользователь {} удалил из друзей {}", userId, friendId);
    }

    @Override
    public List<User> getCommon(Long userId, Long friendId) {
        log.info("Проверка существования пользователя {}", userId);
        userService.getUserById(userId);
        userService.getUserById(friendId);
        log.info("Были запрошены общие друзья пользователей {} и {}", userId, friendId);
        return storage.getCommon(userId, friendId);
    }

    @Override
    public List<User> getUsersFriends(Long id) {
        log.info("Проверка существования пользователя {}", id);
        userService.getUserById(id);
        return storage.getUserFriends(id);
    }
}
