package ru.yandex.practicum.filmorate.service.feed;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

public interface FeedService {
    List<Feed> getAllByUserId(Long userId);
}
