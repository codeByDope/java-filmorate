package ru.yandex.practicum.filmorate.storage.likers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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

    private static final String CHECK_LIKE_EXISTENCE_SQL = "SELECT COUNT(*) FROM likers WHERE film_id = ? AND liker_id = ?";
    private static final String INSERT_LIKE_SQL = "INSERT INTO likers VALUES (?, ?)";
    private static final String DELETE_LIKE_SQL = "DELETE FROM likers WHERE film_id = ? AND liker_id = ?";
    private static final String SELECT_LIKERS_BY_FILM_ID_SQL = "SELECT u.* FROM users u JOIN likers l ON u.id = l.liker_id WHERE l.film_id = ?";
    private static final String CHECK_LIKES_EXISTENCE_FOR_USER_SQL = "SELECT COUNT(*) FROM likers WHERE liker_id = ?";
    private static final String SELECT_FILMS_BY_LIKER_ID_SQL = "SELECT f.* FROM films f JOIN likers l ON f.id = l.film_id WHERE l.liker_id = ?";
    private static final String SELECT_MOST_POPULAR_FILMS_SQL = "SELECT f.* FROM films AS f LEFT JOIN likers AS l ON l.film_id = f.id GROUP BY f.id ORDER BY COUNT(l.film_id) DESC LIMIT ?";

    @Override
    public void add(Long filmId, Long userId) {
        jdbcTemplate.update(INSERT_LIKE_SQL, filmId, userId);
    }

    @Override
    public void delete(Long filmId, Long userId) {
        int count = jdbcTemplate.queryForObject(CHECK_LIKE_EXISTENCE_SQL, Integer.class, filmId, userId);
        if (count == 0) {
            throw new NotLikeException("Пользователь " + userId + " не ставил лайк фильму " + filmId);
        }

        jdbcTemplate.update(DELETE_LIKE_SQL, filmId, userId);
    }

    @Override
    public List<User> getLikersByFilmId(Long id) {
        return jdbcTemplate.query(SELECT_LIKERS_BY_FILM_ID_SQL, new Object[]{id}, userMapper);
    }

    @Override
    public List<Film> getFilmsByLikerId(Long id) {
        int count = jdbcTemplate.queryForObject(CHECK_LIKES_EXISTENCE_FOR_USER_SQL, Integer.class, id);
        if (count == 0) {
            throw new NotLikeException("Пользователь " + id + " не ставил лайков!");
        }

        return jdbcTemplate.query(SELECT_FILMS_BY_LIKER_ID_SQL, new Object[]{id}, filmMapper);
    }

    @Override
    public List<Film> getMostPopularFilms(Long count) {
        return jdbcTemplate.query(SELECT_MOST_POPULAR_FILMS_SQL, new Object[]{count}, filmMapper);
    }
}

