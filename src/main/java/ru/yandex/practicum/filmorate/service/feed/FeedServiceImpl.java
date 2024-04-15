package ru.yandex.practicum.filmorate.service.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.friends.FriendService;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService{
    private final FeedStorage storage;
    private final FriendService friendService;

    @Override
    public List<Feed> getAllByUserId(Long userId) {
        // запрашиваем список всех друзей пользователя
        List<User> friends = friendService.getUsersFriends(userId);

        return List.of();
    }
}
