package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {
    void add(Long mainId, Long friendId);
    void remove(Long mainId, Long friendId);
    List<User> getCommon(Long mainId, Long friendId);
    List<User> getUserFriends(Long id);
}
