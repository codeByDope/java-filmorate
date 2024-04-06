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

    @Override
    public MpaRating add(MpaRating rating) {
        String sql = "INSERT INTO genres VALUES(?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(2, rating.getName());
            return ps;
        }, keyHolder);

        rating.setId(keyHolder.getKey().intValue());
        return rating;
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM MPA_ratings WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<MpaRating> getAll() {
        String sql = "SELECT * FROM MPA_ratings;";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public Optional<MpaRating> getByFilmId(Long id) {
        String sql = "SELECT * \n" +
                "FROM MPA_ratings \n" +
                "WHERE id = (\n" +
                "    SELECT rating_id \n" +
                "    FROM films \n" +
                "    WHERE id = ?\n" +
                ");\n";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);

        if (rs.next()) {
            MpaRating rating = new MpaRating(rs.getInt(1), rs.getString(2));
            return Optional.of(rating);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<MpaRating> getById(Integer id) {
        String sql = "SELECT * \n" +
                "FROM MPA_ratings \n" +
                "WHERE id = ?;";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);

        if (rs.next()) {
            MpaRating rating = new MpaRating(rs.getInt(1), rs.getString(2));
            return Optional.of(rating);
        } else {
            return Optional.empty();
        }
    }
}
