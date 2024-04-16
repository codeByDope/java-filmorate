package ru.yandex.practicum.filmorate.storage.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FeedRowMapper;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FeedEventType;
import ru.yandex.practicum.filmorate.model.FeedOperationType;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedDbStorage implements FeedStorage{
    private final FeedRowMapper mapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Feed add(Long userId, Long entityId, FeedEventType et, FeedOperationType ot) {
        Feed feedToAdd = new Feed();
        String sql = "INSERT INTO feed (time_stamp, user_id, event_type, operation_type, entity_id) VALUES (?, ?, ?, ?, ?)";
        feedToAdd.setUserId(userId);
        feedToAdd.setEntityId(entityId);
        feedToAdd.setTimestamp(Instant.now().toEpochMilli());
        feedToAdd.setEventType(et.name());
        feedToAdd.setOperation(ot.name());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, feedToAdd.getTimestamp());
            ps.setLong(2, feedToAdd.getUserId());
            ps.setString(3, feedToAdd.getEventType());
            ps.setString(4, feedToAdd.getOperation());
            ps.setLong(5, feedToAdd.getEntityId());
            return ps;
        }, keyHolder);

        feedToAdd.setEntityId(keyHolder.getKey().longValue());

        return feedToAdd;
    }

    @Override
    public List<Feed> getAllByUserId(Long userId) {
//        String sql = "SELECT * FROM feed WHERE user_id IN (SELECT friend_user_id FROM friends WHERE main_user_id = ?)";
        String sql = "SELECT * FROM feed WHERE user_id = ?";
        return jdbcTemplate.query(sql, mapper, userId);
    }
}
