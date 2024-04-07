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

    @Override
    public Genre add(Genre genre) {
        String sql = "INSERT INTO genres VALUES(?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(2, genre.getName());
            return ps;
        }, keyHolder);

        genre.setId(keyHolder.getKey().intValue());
        return genre;
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM genres WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genres;";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public List<Genre> getByFilmId(Long id) {
        String sql = "SELECT g.* FROM genres g JOIN films_genres fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, mapper);
    }

    @Override
    public Optional<Genre> getById(Integer id) {
        String sql = "SELECT * FROM genres WHERE id = ?;";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);

        if (rs.next()) {
            Genre genre = new Genre(rs.getInt(1), rs.getString(2));
            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }
}
