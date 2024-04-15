package ru.yandex.practicum.filmorate.service.feed;

import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.util.List;

public interface FeedService {
    Feed add(Long userId, Long entityId, EventType et, OperationType ot);

    List<Feed> getAllByUserId(Long userId);
}
