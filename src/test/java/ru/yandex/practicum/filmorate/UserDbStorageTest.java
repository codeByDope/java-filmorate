package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ExtendWith(SpringExtension.class)
class UserDbStorageTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        UserDbStorage userStorage = new UserDbStorage(new UserRowMapper(), jdbcTemplate);
        userStorage.add(newUser);

        Optional<User> savedUserOptional = userStorage.getById(1L);

        assertThat(savedUserOptional).isPresent();

        User savedUser = savedUserOptional.get();

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(newUser.getId());
        assertThat(savedUser.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(savedUser.getLogin()).isEqualTo(newUser.getLogin());
        assertThat(savedUser.getName()).isEqualTo(newUser.getName());
        assertThat(savedUser.getBirthday()).isEqualTo(newUser.getBirthday());
    }
}
