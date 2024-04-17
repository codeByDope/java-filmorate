package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.mapper.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.PopularFilmsRequestCreator;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private GenreStorage genreStorage;
    private RatingStorage ratingStorage;
    private DirectorStorage directorStorage;
    private PopularFilmsRequestCreator popularFilmsRequestCreator;

    @BeforeEach
    public void setUp() {
        loadTestData();
    }

    private void loadTestData() {
        genreStorage = new GenreDbStorage(new GenreRowMapper(), jdbcTemplate);
        ratingStorage = new RatingDbStorage(new MpaRatingRowMapper(), jdbcTemplate);
        directorStorage = new DirectorDbStorage(new DirectorRowMapper(), jdbcTemplate);

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

        jdbcTemplate.update("MERGE INTO films(id, title, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?, ?)",
                1L, "Test Film", "Test Film Description", LocalDate.of(2023, 1, 1), 120, 1L);
    }

    @Test
    public void testAddFilm() {

        FilmDbStorage filmDbStorage = new FilmDbStorage(new FilmRowMapper(genreStorage, ratingStorage, directorStorage),
                jdbcTemplate, genreStorage, ratingStorage, directorStorage, popularFilmsRequestCreator);

        Optional<Film> gottenFilm = filmDbStorage.getById(1L);

        assertThat(gottenFilm.isPresent());
        assertThat(gottenFilm.get().getName().equals("Test Film"));
    }
}
