package ru.yandex.practicum.filmorate.service.feed;

import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FeedEventType;
import ru.yandex.practicum.filmorate.model.FeedOperationType;

import java.util.List;

public interface FeedService {
    Feed addEvent(Long userId, Long entityId, FeedEventType et, FeedOperationType ot);

    List<Feed> getAllByUserId(Long userId);
}
