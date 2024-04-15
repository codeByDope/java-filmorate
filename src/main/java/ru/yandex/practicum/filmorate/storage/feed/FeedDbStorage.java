package ru.yandex.practicum.filmorate.storage.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedDbStorage implements FeedStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Feed> getAllByUserId(Long userId) {
        return List.of();
    }
}
