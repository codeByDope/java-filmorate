package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.mapper.FeedRowMapper;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FeedEventType;
import ru.yandex.practicum.filmorate.model.FeedOperationType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.feed.FeedDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
public class FeedDbStorageTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private FeedDbStorage feedDbStorage;
    private UserDbStorage userDbStorage;

    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {
        loadTestData();
    }

    public void loadTestData() {
        feedDbStorage = new FeedDbStorage(jdbcTemplate, new FeedRowMapper());
        userDbStorage = new UserDbStorage(new UserRowMapper(), jdbcTemplate);

        user1 = new User(1L, "user1@email.ru", "vanya1", "Ivan Petrov", LocalDate.of(1991, 1, 1));
        user2 = new User(2L, "user2@email.ru", "igor17", "Igor Igorev", LocalDate.of(1989, 1, 13));

        String sql = "INSERT INTO users VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user1.getId(), user1.getEmail(), user1.getLogin(), user1.getName(), Date.valueOf(user1.getBirthday()));
        jdbcTemplate.update(sql, user2.getId(), user2.getEmail(), user2.getLogin(), user2.getName(), Date.valueOf(user2.getBirthday()));

    }

    @Test
    public void testAddFeed() {
        feedDbStorage.add(user1.getId(), user2.getId(), FeedEventType.FRIEND, FeedOperationType.ADD);
        String sql = "select * from feed";
        List<Feed> expectedFeeds = jdbcTemplate.query(sql, new FeedRowMapper());
        assertEquals(expectedFeeds.size(), 1);
    }

    @Test
    public void testgetFeedByUserId() {
        feedDbStorage.add(user1.getId(), user2.getId(), FeedEventType.FRIEND, FeedOperationType.ADD);
        feedDbStorage.add(user2.getId(), user1.getId(), FeedEventType.FRIEND, FeedOperationType.ADD);

        assertEquals(feedDbStorage.getFeedByUserId(1L).get(0).getUserId(), 1L);
        assertEquals(feedDbStorage.getFeedByUserId(1L).get(0).getEntityId(), 2L);
        assertEquals(feedDbStorage.getFeedByUserId(1L).get(0).getEventType(), "FRIEND");
        assertEquals(feedDbStorage.getFeedByUserId(1L).get(0).getOperation(), "ADD");

        assertEquals(feedDbStorage.getFeedByUserId(2L).get(0).getUserId(), 2L);
        assertEquals(feedDbStorage.getFeedByUserId(2L).get(0).getEntityId(), 1L);
        assertEquals(feedDbStorage.getFeedByUserId(2L).get(0).getEventType(), "FRIEND");
        assertEquals(feedDbStorage.getFeedByUserId(2L).get(0).getOperation(), "ADD");
    }
}
