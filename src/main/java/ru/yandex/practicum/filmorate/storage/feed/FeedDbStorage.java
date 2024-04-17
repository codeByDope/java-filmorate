package ru.yandex.practicum.filmorate.storage.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
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
    private final JdbcTemplate jdbcTemplate;

    /**
     * Добавление события в ленту событий.
     * @param userId Id пользователя - автора события
     * @param entityId Id объекта события
     * @param et перечисление, описывающее тип объекта события (REVIEW, LIKE, FRIEND)
     * @param ot перечисление, описывающее событие (ADD, UPDATE, REMOVE)
     * @return сформированный объект Feed
     */
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

    /**
     * Получение ленты событий в виде списка из объектов-событий Feed
     * @param userId Id пользователя, автора событий в ленте
     * @return список из объектов Feed
     */
    @Override
    public List<Feed> getAllByUserId(Long userId) {
        String sql = "SELECT * FROM feed WHERE user_id = ?";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    Feed oneFeed = new Feed();
                    oneFeed.setEventId(resultSet.getLong("EVENT_ID"));
                    oneFeed.setTimestamp(resultSet.getLong("TIME_STAMP"));
                    oneFeed.setUserId(resultSet.getLong("USER_ID"));
                    oneFeed.setEventType(resultSet.getString("EVENT_TYPE"));
                    oneFeed.setOperation(resultSet.getString("OPERATION_TYPE"));
                    oneFeed.setEntityId(resultSet.getLong("ENTITY_ID"));
                    return oneFeed;
                }, userId);
    }
}
