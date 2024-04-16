package ru.yandex.practicum.filmorate.service.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FeedEventType;
import ru.yandex.practicum.filmorate.model.FeedOperationType;
import ru.yandex.practicum.filmorate.service.users.UserService;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService{
    private final FeedStorage storage;
    private final UserService userService;

    @Override
    public Feed addEvent(Long userId, Long entityId, FeedEventType et, FeedOperationType ot) {
        log.info("Пользователь {} добавил в друзья пользователя {}", userId, entityId);
        return storage.add(userId, entityId, et, ot);
    }

    @Override
    public List<Feed> getAllByUserId(Long userId) {
        log.info("Проверка существования пользователя {}", userId);
        userService.getUserById(userId);
        return storage.getAllByUserId(userId);
    }
}
