package ru.yandex.practicum.filmorate.service.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendService {
    void add(Long userId, Long friendId);

    void remove(Long userId, Long friendId);

    List<User> getCommon(Long userId, Long friendId);

    List<User> getUsersFriends(Long id);
}
