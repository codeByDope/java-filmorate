package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.friend.AlreadyFriendsException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendsStorage {
    private final RowMapper<User> mapper;
    private final JdbcTemplate jdbcTemplate;

    private static final String CHECK_FRIENDSHIP_SQL = "SELECT COUNT(*) FROM friends WHERE main_user_id = ? AND friend_user_id = ?";
    private static final String INSERT_FRIENDSHIP_SQL = "INSERT INTO friends VALUES(?, ?)";
    private static final String DELETE_FRIENDSHIP_SQL = "DELETE FROM friends WHERE main_user_id = ? AND friend_user_id = ?";
    private static final String SELECT_COMMON_FRIENDS_SQL = "SELECT *\n" +
            "FROM users AS u\n" +
            "LEFT JOIN friends AS f1 ON u.id = f1.friend_user_id\n" +
            "LEFT JOIN friends AS f2 ON u.id = f2.friend_user_id\n" +
            "WHERE f1.main_user_id = ?\n" +
            "  AND f2.main_user_id = ?";
    private static final String SELECT_USER_FRIENDS_SQL = "SELECT u.*\n" +
            "FROM friends AS f\n" +
            "LEFT JOIN users AS u ON u.id = f.friend_user_id\n" +
            "WHERE f.main_user_id = ?";

    @Override
    public void add(Long mainId, Long friendId) {
        int count = jdbcTemplate.queryForObject(CHECK_FRIENDSHIP_SQL, Integer.class, mainId, friendId);
        if (count == 1) {
            throw new AlreadyFriendsException("Пользователь с id " + friendId + " уже является другом пользователя с id " + mainId);
        }

        jdbcTemplate.update(INSERT_FRIENDSHIP_SQL, mainId, friendId);
    }

    @Override
    public void remove(Long mainId, Long friendId) {
        jdbcTemplate.update(DELETE_FRIENDSHIP_SQL, mainId, friendId);
    }

    @Override
    public List<User> getCommon(Long mainId, Long friendId) {
        return jdbcTemplate.query(SELECT_COMMON_FRIENDS_SQL, new Object[]{mainId, friendId}, mapper);
    }

    @Override
    public List<User> getUserFriends(Long id) {
        List<User> friends = jdbcTemplate.query(SELECT_USER_FRIENDS_SQL, new Object[]{id}, mapper);
        log.info("Выполнен запрос на получение друзей к БД, результат: {}", friends);
        return friends;
    }
}
