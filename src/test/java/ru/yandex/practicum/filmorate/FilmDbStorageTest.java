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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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


    @BeforeEach
    public void setUp() {
        loadTestData();
    }

    private void loadTestData() {
        genreStorage = new GenreDbStorage(new GenreRowMapper(), jdbcTemplate);
        ratingStorage = new RatingDbStorage(new MpaRatingRowMapper(), jdbcTemplate);


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
    }

    @Test
    public void testAddFilm() {

        Film film = new Film.Builder()
                .name("Test Film")
                .description("Test Film Description")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .duration(120)
                .genres(genreStorage.getAll())
                .mpa(ratingStorage.getById(1).get())
                .build();

        FilmDbStorage filmDbStorage = new FilmDbStorage(new FilmRowMapper(genreStorage, ratingStorage), jdbcTemplate, genreStorage, ratingStorage);

        Optional<Film> gottenFilm = filmDbStorage.getById(1L);

        assertThat(gottenFilm.isPresent());
        assertThat(gottenFilm.get().getName().equals("Test Film"));
    }
}
