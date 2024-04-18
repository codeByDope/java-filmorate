package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FeedEventType;
import ru.yandex.practicum.filmorate.model.FeedOperationType;

import java.util.List;

public interface FeedStorage {
    Feed add(Long userId, Long entityId, FeedEventType et, FeedOperationType ot);

    List<Feed> getFeedByUserId(Long userId);
}
