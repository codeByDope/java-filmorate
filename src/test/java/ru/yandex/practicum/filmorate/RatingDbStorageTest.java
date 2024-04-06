package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.mapper.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingDbStorageTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private RatingStorage ratingStorage;

    @BeforeEach
    public void setUp() {
        loadTestData();
    }

    private void loadTestData() {
        ratingStorage = new ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage(new MpaRatingRowMapper(), jdbcTemplate);
        jdbcTemplate.execute("MERGE INTO mpa_ratings(id, title) VALUES (1, 'G')");
        jdbcTemplate.execute("MERGE INTO mpa_ratings(id, title) VALUES (2, 'PG')");
        jdbcTemplate.execute("MERGE INTO mpa_ratings(id, title) VALUES (3, 'PG-13')");
        jdbcTemplate.execute("MERGE INTO mpa_ratings(id, title) VALUES (4, 'R')");
        jdbcTemplate.execute("MERGE INTO mpa_ratings(id, title) VALUES (5, 'NC-17')");
    }

    @Test
    public void testGetRatingById() {
        assertTrue("R".equals(ratingStorage.getById(4).get().getName()));
    }
}
