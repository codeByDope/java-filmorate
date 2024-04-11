package ru.yandex.practicum.filmorate.storage.likers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.like.LikeHasAlreadyCreatedException;
import ru.yandex.practicum.filmorate.exception.like.NotLikeException;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikersDbStorage implements LikerStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userMapper;
    private final FilmRowMapper filmMapper;

    @Override
    public void add(Long filmId, Long userId) {
        String checkSql = "SELECT COUNT(*) FROM likers WHERE film_id = ? AND liker_id = ?;";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, filmId, userId);
        if (count == 1) {
            throw new LikeHasAlreadyCreatedException("Пользователь " + userId + " уже поставил лайк фильму " + filmId);
        }

        String sql = "INSERT INTO likers VALUES(?,?);";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void delete(Long filmId, Long userId) {
        String checkSql = "SELECT COUNT(*) FROM likers WHERE film_id = ? AND liker_id = ?;";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, filmId, userId);
        if (count == 0) {
            throw new NotLikeException("Пользователь " + userId + " не ставил лайк фильму " + filmId);
        }

        String sql = "DELETE FROM likers WHERE film_id = ? AND liker_id = ?;";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<User> getLikersByFilmId(Long id) {
        String sql = "SELECT u.* FROM users u JOIN likers l ON u.id = l.liker_id WHERE l.film_id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, userMapper);
    }


    @Override
    public List<Film> getFilmsByLikerId(Long id) {
        String checkSql = "SELECT COUNT(*) FROM likers WHERE liker_id = ?;";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        if (count == 0) {
            throw new NotLikeException("Пользователь " + id + " не ставил лайков!");
        }

        String sql = "SELECT f.* FROM films f JOIN likers l ON f.id = l.film_id WHERE l.liker_d = ?;";
        return jdbcTemplate.query(sql, new Object[]{id}, filmMapper);
    }

    @Override
    public List<Film> getMostPopularFilms(Long count) {
        String sql = "SELECT f.* " +
                "FROM films AS f " +
                "LEFT JOIN likers as l on l.film_id = f.id " +
                "GROUP BY f.id " +
                "ORDER BY  COUNT(l.film_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{count}, filmMapper);
    }
}
