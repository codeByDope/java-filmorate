package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FilmTests {
    @Autowired
    Validator validator;

    @Test
    public void allGoodTest() {
        Film film = new Film();
        film.setId(1L);
        film.setName("film");
        film.setDescription("description))");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());
    }

    @Test
    public void badName() {
        Film film = new Film();
        film.setId(1L);
        film.setName("");
        film.setDescription("description))");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }

    @Test
    public void longDescription() {
        Film film = new Film();
        film.setId(1L);
        film.setName("film");
        film.setDescription("description))".repeat(20));
        film.setReleaseDate(LocalDate.now());
        film.setDuration(1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }

    @Test
    public void negativeDuration() {
        Film film = new Film();
        film.setId(1L);
        film.setName("film");
        film.setDescription("description))");
        film.setReleaseDate(LocalDate.of(1600, 10, 1));
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }
}
