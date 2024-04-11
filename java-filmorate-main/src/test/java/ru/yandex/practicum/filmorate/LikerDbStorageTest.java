package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.mapper.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.likers.LikerStorage;
import ru.yandex.practicum.filmorate.storage.likers.LikersDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikerDbStorageTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LikerStorage likerStorage;
    private FilmStorage filmStorage;
    private GenreStorage genreStorage;
    private RatingStorage ratingStorage;
    private UserStorage userStorage;


    @BeforeEach
    public void setUp() {
        loadTestData();
    }

    private void loadTestData() {
        genreStorage = new GenreDbStorage(new GenreRowMapper(), jdbcTemplate);
        ratingStorage = new RatingDbStorage(new MpaRatingRowMapper(), jdbcTemplate);
        likerStorage = new LikersDbStorage(jdbcTemplate, new UserRowMapper(), new FilmRowMapper(genreStorage, ratingStorage));
        userStorage = new UserDbStorage(new UserRowMapper(), jdbcTemplate);
        filmStorage = new FilmDbStorage(new FilmRowMapper(genreStorage, ratingStorage), jdbcTemplate, genreStorage, ratingStorage);

        jdbcTemplate.execute("MERGE INTO genres(id, title) VALUES (1, 'Комедия')");
        jdbcTemplate.execute("MERGE INTO genres(id, title) VALUES (2, 'Драма')");
        jdbcTemplate.execute("MERGE INTO genres(id, title) VALUES (3, 'Мультфильм')");
        jdbcTemplate.execute("MERGE INTO genres(id, title) VALUES (4, 'Триллер')");
        jdbcTemplate.execute("MERGE INTO genres(id, title) VALUES (5, 'Документальный')");
        jdbcTemplate.execute("MERGE INTO genres(id, title) VALUES (6, 'Боевик')");

        jdbcTemplate.execute("MERGE INTO mpa_ratings(id, title) VALUES (1, 'G')");
        jdbcTemplate.execute("MERGE INTO mpa_ratings(id, title) VALUES (2, 'PG')");
        jdbcTemplate.execute("MERGE INTO mpa_ratings(id, title) VALUES (3, 'PG-13')");
        jdbcTemplate.execute("MERGE INTO mpa_ratings(id, title) VALUES (4, 'R')");
        jdbcTemplate.execute("MERGE INTO mpa_ratings(id, title) VALUES (5, 'NC-17')");

        User newUser = new User(1L, "user1@email.ru", "vanya1", "Ivan Petrov", LocalDate.of(1991, 1, 1));
        User newUser1 = new User(2L, "user2@email.ru", "vanya2", "Ivan Petrov", LocalDate.of(1992, 2, 2));
        User newUser2 = new User(3L, "user3@email.ru", "vanya3", "Ivan Petrov", LocalDate.of(1993, 3, 3));
        String sql = "INSERT INTO users VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, newUser.getId(), newUser.getEmail(), newUser.getLogin(), newUser.getName(), Date.valueOf(newUser.getBirthday()));
        jdbcTemplate.update(sql, newUser1.getId(), newUser1.getEmail(), newUser1.getLogin(), newUser1.getName(), Date.valueOf(newUser1.getBirthday()));
        jdbcTemplate.update(sql, newUser2.getId(), newUser2.getEmail(), newUser2.getLogin(), newUser2.getName(), Date.valueOf(newUser2.getBirthday()));

        jdbcTemplate.update("MERGE INTO films(id, title, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?, ?)",
                1L, "Test Film", "Test Film Description", LocalDate.of(2023, 1, 1), 120, 1L);

        String sqlLiker = "INSERT INTO likers VALUES(?,?);";
        jdbcTemplate.update(sqlLiker, 1L, 1L);
        jdbcTemplate.update(sqlLiker, 1L, 2L);
        jdbcTemplate.update(sqlLiker, 1L, 3L);

    }

    @Test
    public void testLiker() {
        assertEquals(3, likerStorage.getLikersByFilmId(1L).size());
    }
}
