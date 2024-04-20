package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.utils.ApiPathConstants;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.friends.FriendService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(ApiPathConstants.USER_PATH)
@RequiredArgsConstructor
public class FriendsController {
    private final FriendService service;

    @PutMapping(ApiPathConstants.FRIENDS_PATH_BY_ID_AND_FRIEND_ID)
    public Boolean add(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Пользователь {} создал запрос на добавление пользователя {} в друзья", id, friendId);
        service.add(id, friendId);
        return true;
    }

    @DeleteMapping(ApiPathConstants.FRIENDS_PATH_BY_ID_AND_FRIEND_ID)
    public Boolean remove(@PathVariable Long id, @PathVariable Long friendId) {
        service.remove(id, friendId);
        return true;
    }

    @GetMapping(ApiPathConstants.FRIENDS_PATH_BY_ID)
    public List<User> getUsersFriends(@PathVariable Long id) {
        log.info("Запрос на получение друзей пользователя {}", id);
        List<User> friends = service.getUsersFriends(id);
        log.info("Список друзей пользователя {}: {}", id, friends);
        return friends;
    }

    @GetMapping(ApiPathConstants.COMMON_FRIENDS_PATH)
    public List<User> getCommon(@PathVariable Long id, @PathVariable Long otherId) {
        return service.getCommon(id, otherId);
    }
}
