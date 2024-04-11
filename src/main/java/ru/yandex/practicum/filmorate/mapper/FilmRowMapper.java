package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;
    private final DirectorStorage directorStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        Long id = rs.getLong("id");
        film.setId(id);
        film.setName(rs.getString("title"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(LocalDate.parse(rs.getString("release_date")));
        film.setDuration(rs.getLong("duration"));

        MpaRating rating = ratingStorage.getById(rs.getInt("rating_id")).orElse(null);
        film.setMpa(rating);

        Set<Genre> genres = new HashSet<>(genreStorage.getByFilmId(id));
        film.setGenres(genres);

        Set<Director> directors = new HashSet<>(directorStorage.getAllFilmDirectors(id));
        film.setDirectors(directors);

        return film;
    }
}
