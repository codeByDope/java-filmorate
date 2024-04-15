package ru.yandex.practicum.filmorate.storage.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedDbStorage implements FeedStorage{

    @Override
    public void add(Long userId, EventType et, OperationType ot) {
        String sql = "INSERT INTO feed (timestamp, user_id, event_type, operation, entity_id) VALUES (?, ?, ?, ?, ?)";
        LocalDateTime localDateTime = LocalDateTime.now();



    }

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Feed> getAllByUserId(Long userId) {
        return List.of();
    }
}
