package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserTests {
    @Autowired
    Validator validator;

    @Test
    public void allGoodTest() {
        User user = new User();
        user.setId(1L);
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
        user.setId(1L);
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
        user.setId(1L);
        user.setEmail("kikiloveme@mail.ru");
        user.setLogin(" ");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("Kiruha");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void futureBirthdayTest() {
        User user = new User();
        user.setId(1L);
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
        user.setId(1L);
        user.setEmail("kikilovememail.ru");
        user.setLogin("");
        user.setBirthday(LocalDate.of(2100, 1, 1));
        user.setName("Kiruha");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(3, violations.size());
    }
}
