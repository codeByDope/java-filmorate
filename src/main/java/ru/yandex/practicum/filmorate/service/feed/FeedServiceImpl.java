package ru.yandex.practicum.filmorate.service.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService{
    // todo: добавить FeedStorage компонент

    @Override
    public List<Feed> getAllByUserId(Long userId) {
        return List.of();
    }
}
