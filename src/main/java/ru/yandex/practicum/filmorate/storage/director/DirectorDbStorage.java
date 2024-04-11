package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcOperations jdbcOperations;
    private final RowMapper<Director> mapper;
    private static final String SQL_GET_ALL = "select * from directors";
    private static final String SQL_GET_BY_ID = "select * from directors where id = ?";
    private static final String SQL_ADD = "insert into directors (name) values (?)";
    private static final String SQL_UPDATE = "update directors set name = ? where id = ?";
    private static final String SQL_DELETE = "delete from directors where id = ?";
    private static final String SQL_GET_ALL_FILMS_DIRECTORS = "select * from directors as d " +
            "join films_directors as fd on fd.director_id = d.id " +
            "join films as f on f.id = fd.film_id " +
            "where f.id = ? " +
            "order by fd.director_id";

    @Override
    public List<Director> getAll() {
        return jdbcOperations.query(SQL_GET_ALL, mapper);
    }

    @Override
    public Director getById(int id) {
        List<Director> directors = jdbcOperations.query(SQL_GET_BY_ID, mapper, id);
        if (directors.size() != 1) {
            return null;
        }
        return directors.get(0);
    }

    @Override
    public Director add(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_ADD, new String[]{"id"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        director.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return director;
    }

    @Override
    public Director update(Director director) {
        jdbcOperations.update(SQL_UPDATE, director.getName(), director.getId());
        return director;
    }

    @Override
    public void delete(int id) {

        jdbcOperations.update("delete from films_directors where director_id = ?", id);
        jdbcOperations.update(SQL_DELETE, id);
    }

    @Override
    public Set<Director> getByIds(List<Integer> ids) {
        Set<Director> genres = new HashSet<>();
        ids.forEach(id -> {
            Director director = getById(id);
            if (director != null) {
                genres.add(director);
            }
        });
        return genres;
    }

    @Override
    public Set<Director> getAllFilmDirectors(Long filmId) {
        return new HashSet<>(jdbcOperations.query(SQL_GET_ALL_FILMS_DIRECTORS, mapper, filmId));
    }
}