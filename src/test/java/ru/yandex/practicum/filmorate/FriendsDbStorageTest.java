package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendsDbStorageTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UserStorage userStorage;
    private FriendsStorage friendsStorage;

    @BeforeEach
    public void setUp() {
        loadTestData();
    }

    private void loadTestData() {
        userStorage = new UserDbStorage(new UserRowMapper(), jdbcTemplate);
        friendsStorage = new ru.yandex.practicum.filmorate.storage.friends.FriendsDbStorage(new UserRowMapper(), jdbcTemplate);
    }

    @Test
    public void testGetUsersFriends() {
        User newUser = new User(1L, "user1@email.ru", "vanya1", "Ivan Petrov", LocalDate.of(1991, 1, 1));
        User newUser1 = new User(2L, "user2@email.ru", "vanya2", "Ivan Petrov", LocalDate.of(1992, 2, 2));
        User newUser2 = new User(3L, "user3@email.ru", "vanya3", "Ivan Petrov", LocalDate.of(1993, 3, 3));

        userStorage.add(newUser);
        userStorage.add(newUser1);
        userStorage.add(newUser2);

        friendsStorage.add(1L, 2L);
        friendsStorage.add(1L, 3L);

        assertEquals(2, friendsStorage.getUserFriends(1L).size());
    }

}
