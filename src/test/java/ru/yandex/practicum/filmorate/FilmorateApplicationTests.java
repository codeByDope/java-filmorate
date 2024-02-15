package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class FilmorateApplicationTests {
    @Autowired
    Validator validator;

    @Test
    public void allGoodTest() {
        User user = new User();
        user.setId(1);
        user.setEmail("kikiloveme@mail.ru");
        user.setLogin("kikidoulovemi");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("Kiruha");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void badEmailTest() {
        User user = new User();
        user.setId(1);
        user.setEmail("kikiloveme.mail.ru");
        user.setLogin("kikidoulovemi");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("Kiruha");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void badLoginTest() {
        User user = new User();
        user.setId(1);
        user.setEmail("kikiloveme@mail.ru");
        user.setLogin(" ");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("Kiruha");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void badLoginWithWhitespaceTest() {
        UserController controller = new UserController();
        User user = new User();
        user.setId(1);
        user.setEmail("kikiloveme@mail.ru");
        user.setLogin("ki ki");
        user.setBirthday(LocalDate.of(2100, 1, 1));
        user.setName("Kiruha");
        controller.addUser(user);


        assertEquals(ResponseEntity.badRequest().body(user), controller.addUser(user));
    }

    @Test
    public void futureBirthdayTest() {
        User user = new User();
        user.setId(1);
        user.setEmail("kikiloveme@mail.ru");
        user.setLogin("kiki");
        user.setBirthday(LocalDate.of(2100, 1, 1));
        user.setName("Kiruha");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void allBadTest() {
        User user = new User();
        user.setId(1);
        user.setEmail("kikilovememail.ru");
        user.setLogin("");
        user.setBirthday(LocalDate.of(2100, 1, 1));
        user.setName("Kiruha");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(3, violations.size());
    }

    @Test
    public void filmAllGoodTest() {
        Film film = new Film();
        film.setId(1);
        film.setName("film");
        film.setDescription("description))");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());
    }

    @Test
    public void filmBadLogin() {
        Film film = new Film();
        film.setId(1);
        film.setName("");
        film.setDescription("description))");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }

    @Test
    public void filmLongDescription() {
        Film film = new Film();
        film.setId(1);
        film.setName("film");
        film.setDescription("description))".repeat(20));
        film.setReleaseDate(LocalDate.now());
        film.setDuration(1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }

    @Test
    public void filmTooPastRelease() {
        FilmController controller = new FilmController();
        Film film = new Film();
        film.setId(1);
        film.setName("film");
        film.setDescription("description))");
        film.setReleaseDate(LocalDate.of(1600, 10, 1));
        film.setDuration(1);

        assertEquals(ResponseEntity.badRequest().body(film), controller.addFilm(film));
    }

    @Test
    public void filmNegativeDuration() {
        Film film = new Film();
        film.setId(1);
        film.setName("film");
        film.setDescription("description))");
        film.setReleaseDate(LocalDate.of(1600, 10, 1));
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }
}
