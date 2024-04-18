package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FeedRowMapper implements RowMapper<Feed> {
    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feed feed = new Feed();

        feed.setEventId(rs.getLong("event_id"));
        feed.setTimestamp(rs.getLong("time_stamp"));
        feed.setUserId(rs.getLong("user_id"));
        feed.setEventType(rs.getString("event_type"));
        feed.setOperation(rs.getString("operation_type"));
        feed.setEntityId(rs.getLong("entity_id"));

        return feed;
    }
}
