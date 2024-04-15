package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import javax.validation.ValidationException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final FilmRowMapper mapper;
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;
    private final DirectorStorage directorStorage;

    private static final String INSERT_FILM_SQL = "INSERT INTO films (title, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_FILM_GENRE_SQL = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String UPDATE_FILM_SQL = "UPDATE films SET title = ?, description = ?, release_date = ?, " +
            "duration = ?, rating_id = ? WHERE id = ?";
    private static final String DELETE_GENRES_SQL = "DELETE FROM films_genres WHERE film_id = ?";
    private static final String DELETE_FILM_SQL = "DELETE FROM films WHERE id = ?";
    private static final String SELECT_ALL_FILMS_SQL = "SELECT * FROM films";
    private static final String SELECT_FILM_BY_ID_SQL = "SELECT * FROM films WHERE id = ?";
    private static final String SQL_GET_DIRECTOR_FILMS_SORTED_BY_LIKES = "select f.*, m.* from films as f " +
            "join mpa_ratings as m on m.id = f.rating_id " +
            "left join likers as l on l.film_id = f.id " +
            "join films_directors as fd on fd.film_id = f.id " +
            "where fd.director_id = ? " +
            "group by f.id " +
            "order by count(l.film_id) desc";

    private static final String SQL_GET_DIRECTOR_FILMS_SORTED_BY_YEARS = "select f.*, m.* from films as f " +
            "join mpa_ratings as m on m.id = f.rating_id " +
            "join films_directors as fd on fd.film_id = f.id " +
            "where fd.director_id = ? " +
            "order by f.release_date";

    private static final String SQL_ADD_DIRECTORS = "insert into films_directors (film_id, director_id) values (?, ?)";
    private static final String SQL_DELETE_DIRECTORS = "delete from films_directors where film_id = ?";


    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (ratingStorage.getById(film.getMpa().getId()).isEmpty()) {
            throw new ValidationException("Рейтинга с таким ID не существует!");
        }
        for (Genre genre : film.getGenres()) {
            genreStorage.getById(genre.getId()).orElseThrow(() -> new ValidationException("Неправильно набран рейтинг!"));
        }

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_FILM_SQL, Statement.RETURN_GENERATED_KEYS);
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
            jdbcTemplate.update(INSERT_FILM_GENRE_SQL, filmId, genre.getId());
        }

        film.setId(filmId);
        updateDirectors(film);
        return film;
    }


    @Override
    public Film update(Film film) {
        Long filmId = film.getId();

        List<Genre> genres = film.getGenres().stream().distinct().collect(Collectors.toList());
        for (Genre genre : genres) {
            Genre g = genreStorage.getById(genre.getId()).orElseThrow(() ->
                    new ValidationException("Неправильно набран рейтинг!"));
            genre.setName(g.getName());
        }
        film.setGenres(genres.stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toList()));

        jdbcTemplate.update(DELETE_GENRES_SQL, filmId);

        jdbcTemplate.update(UPDATE_FILM_SQL, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpa().getId(), filmId);

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(INSERT_FILM_GENRE_SQL, filmId, genre.getId());
        }
        updateDirectors(film);
        return film;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_FILM_SQL, id);
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(SELECT_ALL_FILMS_SQL, mapper);
    }

    @Override
    public Optional<Film> getById(Long id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_FILM_BY_ID_SQL, id);

        if (rs.next()) {
            Film film = new Film.Builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("title"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .mpa(ratingStorage.getByFilmId(id).orElse(null))
                    .genres(genreStorage.getByFilmId(id))
                    .directors(directorStorage.getAllFilmDirectors(id))
                    .build();

            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getDirectorsFilmSortedByLikes(int directorId) {
        return jdbcTemplate.query(SQL_GET_DIRECTOR_FILMS_SORTED_BY_LIKES, mapper, directorId);
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

    @Override
    public List<Film> getDirectorsFilmSortedByYears(int directorId) {
        return jdbcTemplate.query(SQL_GET_DIRECTOR_FILMS_SORTED_BY_YEARS, mapper, directorId);
    }

    private void updateDirectors(Film film) {
        jdbcTemplate.update(SQL_DELETE_DIRECTORS, film.getId());
        List<Director> directors = film.getDirectors();
        directors.forEach(x -> jdbcTemplate.update(SQL_ADD_DIRECTORS, film.getId(), x.getId()));
    }
}