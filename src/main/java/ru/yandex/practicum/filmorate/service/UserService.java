package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(Long id, Long friendId) {
        storage.getUserById(id).addFriend(friendId);
        storage.getUserById(friendId).addFriend(id);
        log.info("Пользователь {} добавил в друзья пользователя {}.",id,friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        storage.getUserById(id).removeFriend(friendId);
        storage.getUserById(friendId).removeFriend(id);
        log.info("Пользователь {} удалил из друзей пользователя {}.",id,friendId);
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        Collection<User> result = new HashSet<>();
        Set<Long> idFriends = storage.getUserById(id).getFriendsId();
        Set<Long> otherIdFriends = storage.getUserById(otherId).getFriendsId();
        for (Long friendId : idFriends) {
            if (otherIdFriends.contains(friendId)) {
                result.add(storage.getUserById(friendId));
            }
        }
        log.info("Пользователь {} запросил список общих друзей с пользователем {}", id, otherId);
        return result;
    }

    public Collection<User> getUsersFriends(Long id) {
        Set<Long> setOfFriends = storage.getUserById(id).getFriendsId();
        Collection<User> result = new ArrayList<>();
        for (Long friendId: setOfFriends) {
            result.add(storage.getUserById(friendId));
        }
        return result;
    }
}
