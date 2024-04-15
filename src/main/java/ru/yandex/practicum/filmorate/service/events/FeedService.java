package ru.yandex.practicum.filmorate.service.events;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface FeedService {
    List<Event> getFeed();
}
