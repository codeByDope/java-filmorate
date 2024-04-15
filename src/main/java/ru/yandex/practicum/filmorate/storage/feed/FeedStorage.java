package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.util.List;

public interface FeedStorage {
    void add(Long userId, EventType et, OperationType ot);

    List<Feed> getAllByUserId(Long userId);
}
