package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import javax.validation.ValidationException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final RowMapper<Film> mapper;
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;

    @Override
    public Film add(Film film) {
        String insertFilmSql = "INSERT INTO films (title, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        String insertFilmGenreSql = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (ratingStorage.getById(film.getMpa().getId()).isEmpty()) {
            throw new ValidationException("Рейтинга с таким ID не существует!");
        }
        for (Genre genre : film.getGenres()) {
            genreStorage.getById(genre.getId()).orElseThrow(() -> new ValidationException("Неправильно набран рейтинг!"));
        }

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertFilmSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            if (film.getMpa() != null) {
                ps.setInt(5, film.getMpa().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            return ps;
        }, keyHolder);

        Long filmId = keyHolder.getKey().longValue();

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(insertFilmGenreSql, filmId, genre.getId());
        }

        film.setId(filmId);
        return film;
    }


    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET title = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?;";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM films WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM films;";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public Optional<Film> getById(Long id) {
        String sql = "SELECT * FROM films WHERE id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);

        if (rs.next()) {
            Film film = new Film.Builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("title"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .mpa(ratingStorage.getByFilmId(id).orElse(null))
                    .genres(genreStorage.getByFilmId(id))
                    .build();

            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getMostPopularFilms(Long count) {
        String sql = "SELECT f.*\n" +
                "FROM films AS f\n" +
                "INNER JOIN (\n" +
                "    SELECT film_id, COUNT(liker_id) AS count_likes\n" +
                "    FROM likers\n" +
                "    GROUP BY film_id\n" +
                "    ORDER BY count_likes DESC\n" +
                "    LIMIT ?\n" +
                ") AS top_films ON f.id = top_films.film_id;";
        return jdbcTemplate.query(sql, new Object[]{count}, mapper);
    }

    @Override
    public Collection<Film> getPopular(Integer limit, Integer genreId, Integer year) {
        String sql = String.format("SELECT f.*, COUNT(l.liker_id) AS like_count " +
                "FROM films f " +
                "JOIN likers l ON f.id = l.film_id " +
                "WHERE f.genreId=%d AND EXTRACT(YEAR FROM release_date)=%d " +
                "ORDER BY like_count DESC " +
                "LIMIT %d", genreId, year, limit);
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, limit, genreId, year);
        Collection<Film> filmsSortedByPopularity = new ArrayList<>();
        while (rs.next()) {
            Film film = new Film.Builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("title"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .mpa(ratingStorage.getByFilmId(rs.getLong("id")).orElse(null))
                    .genres(genreStorage.getByFilmId(rs.getLong("id")))
                    .build();
            filmsSortedByPopularity.add(film);
        }
        return filmsSortedByPopularity;
    }
}
