package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private GenreStorage genreStorage;

    @BeforeEach
    public void setUp() {
        loadTestData();
    }

    private void loadTestData() {
        genreStorage = new GenreDbStorage(new GenreRowMapper(), jdbcTemplate);

        jdbcTemplate.execute("MERGE INTO genres(id, title) VALUES (1, 'Комедия')");
        jdbcTemplate.execute("MERGE INTO genres(id, title) VALUES (2, 'Драма')");
        jdbcTemplate.execute("MERGE INTO genres(id, title) VALUES (3, 'Мультфильм')");
        jdbcTemplate.execute("MERGE INTO genres(id, title) VALUES (4, 'Триллер')");
        jdbcTemplate.execute("MERGE INTO genres(id, title) VALUES (5, 'Документальный')");
        jdbcTemplate.execute("MERGE INTO genres(id, title) VALUES (6, 'Боевик')");
    }

    @Test
    public void testGetGenreById() {
        assertTrue("Триллер".equals(genreStorage.getById(4).get().getName()));
    }
}
