package ru.yandex.practicum.filmorate.storage.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {
    private final RowMapper<MpaRating> mapper;
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_RATING_SQL = "INSERT INTO genres VALUES (?, ?)";
    private static final String DELETE_RATING_SQL = "DELETE FROM MPA_ratings WHERE id = ?";
    private static final String SELECT_ALL_RATINGS_SQL = "SELECT * FROM MPA_ratings";
    private static final String SELECT_RATING_BY_FILM_ID_SQL = "SELECT mpa.* FROM MPA_ratings mpa JOIN films f ON mpa.id = f.rating_id WHERE f.id = ?";
    private static final String SELECT_RATING_BY_ID_SQL = "SELECT * FROM MPA_ratings WHERE id = ?";

    @Override
    public MpaRating add(MpaRating rating) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_RATING_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(2, rating.getName());
            return ps;
        }, keyHolder);

        rating.setId(keyHolder.getKey().intValue());
        return rating;
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update(DELETE_RATING_SQL, id);
    }

    @Override
    public List<MpaRating> getAll() {
        return jdbcTemplate.query(SELECT_ALL_RATINGS_SQL, mapper);
    }

    @Override
    public Optional<MpaRating> getByFilmId(Long id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_RATING_BY_FILM_ID_SQL, id);

        if (rs.next()) {
            MpaRating rating = new MpaRating(rs.getInt(1), rs.getString(2));
            return Optional.of(rating);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<MpaRating> getById(Integer id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_RATING_BY_ID_SQL, id);

        if (rs.next()) {
            MpaRating rating = new MpaRating(rs.getInt(1), rs.getString(2));
            return Optional.of(rating);
        } else {
            return Optional.empty();
        }
    }
}
