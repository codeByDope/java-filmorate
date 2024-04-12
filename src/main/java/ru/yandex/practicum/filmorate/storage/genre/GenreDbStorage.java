package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final RowMapper<Genre> mapper;
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_GENRE_SQL = "INSERT INTO genres VALUES(?, ?)";
    private static final String DELETE_GENRE_SQL = "DELETE FROM genres WHERE id = ?";
    private static final String SELECT_ALL_GENRES_SQL = "SELECT * FROM genres";
    private static final String SELECT_GENRES_BY_FILM_ID_SQL = "SELECT DISTINCT g.* FROM genres g JOIN films_genres fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
    private static final String SELECT_GENRE_BY_ID_SQL = "SELECT * FROM genres WHERE id = ?";

    @Override
    public Genre add(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_GENRE_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, genre.getId());
            ps.setString(2, genre.getName());
            return ps;
        }, keyHolder);

        genre.setId(keyHolder.getKey().intValue());
        return genre;
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update(DELETE_GENRE_SQL, id);
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(SELECT_ALL_GENRES_SQL, mapper);
    }

    @Override
    public List<Genre> getByFilmId(Long id) {
        return jdbcTemplate.query(SELECT_GENRES_BY_FILM_ID_SQL, new Object[]{id}, mapper);
    }

    @Override
    public Optional<Genre> getById(Integer id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_GENRE_BY_ID_SQL, id);

        if (rs.next()) {
            Genre genre = new Genre(rs.getInt(1), rs.getString(2));
            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }
}
