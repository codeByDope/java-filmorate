package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.director.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {
    private final DirectorRowMapper mapper;
    private final JdbcTemplate jdbcTemplate;

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
        return jdbcTemplate.query(SQL_GET_ALL, mapper);
    }

    @Override
    public Optional<Director> getById(int id) {
        List<Director> directors = jdbcTemplate.query(SQL_GET_BY_ID, mapper, id);
        if (directors.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(directors.get(0));
    }

    @Override
    public Director add(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_ADD, new String[]{"id"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return getById(id)
                .orElseThrow(() -> new DirectorNotFoundException("Режиссёрa с таким id не существует"));
    }

    @Override
    public Director update(Director director) {
        jdbcTemplate.update(SQL_UPDATE, director.getName(), director.getId());
        return director;
    }

    @Override
    public void delete(int id) {

        jdbcTemplate.update("delete from films_directors where director_id = ?", id);
        jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public List<Director> getByIds(List<Integer> ids) {
        List<Director> directors = new ArrayList<>();
        ids.forEach(id -> {
            Optional<Director> director = getById(id);
            director.ifPresent(directors::add);
        });
        return directors;
    }

    @Override
    public List<Director> getAllFilmDirectors(Long filmId) {
        return new ArrayList<>(jdbcTemplate.query(SQL_GET_ALL_FILMS_DIRECTORS, mapper, filmId));
    }
}