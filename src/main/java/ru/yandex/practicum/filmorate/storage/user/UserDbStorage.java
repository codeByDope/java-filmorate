package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Component
@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final RowMapper<User> mapper;
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_USER_SQL = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String INSERT_USER_WITH_ID_SQL = "INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_SQL = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM users";
    private static final String SELECT_USER_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_ALL_FROM_LIKERS_SQL = "SELECT * FROM likers";

    @Override
    public User add(User user) {
        if (user.getId() == null) {
            return addWithGeneratedId(user);
        } else {
            return addWithSpecifiedId(user);
        }
    }

    private User addWithGeneratedId(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String name = selectName(user);

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, name);
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        user.setName(name); // Обновляем имя пользователя на логин, если оно пустое
        return user;
    }

    private User addWithSpecifiedId(User user) {
        jdbcTemplate.update(INSERT_USER_WITH_ID_SQL, user.getId(), user.getEmail(), user.getLogin(),
                selectName(user), Date.valueOf(user.getBirthday()));
        return user;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(UPDATE_USER_SQL, user.getEmail(), user.getLogin(), user.getName(),
                Date.valueOf(user.getBirthday()), user.getId());
        return user;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_USER_SQL, id);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(SELECT_ALL_USERS_SQL, mapper);
    }

    @Override
    public Optional<User> getById(Long id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_USER_BY_ID_SQL, id);

        if (rs.next()) {
            User user = new User.Builder()
                    .id(id)
                    .email(rs.getString(2))
                    .login(rs.getString(3))
                    .name(rs.getString(4))
                    .birthday(rs.getDate(5).toLocalDate())
                    .build();

            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Long> getRecommendations(Long id) {
        Map<Long, List<Long>> likerToFilmsMap = new HashMap<>();
        Integer max = 0;
        List<Long> result = new ArrayList<>();

        // Заполняем likersToFilmsMap: Ключ - id лайкнувшего, Значение - список лайкнутых фильмов
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_ALL_FROM_LIKERS_SQL);
        while (rs.next()) {
            Long likerId = rs.getLong(2);
            Long filmId = rs.getLong(1);
            if (likerToFilmsMap.containsKey(likerId)) {
                likerToFilmsMap.get(likerId).add(filmId);
            } else {
                List<Long> films = new ArrayList<>();
                films.add(filmId);
                likerToFilmsMap.put(likerId, films);
            }
        }

        // Получаем список лайков пользователя, которому подбираем рекомендации
        List<Long> likesOfUser = likerToFilmsMap.get(id);
        /* Перебираем остальных юзеров, считаем количество сходств и одновременно сохраняем
        нелайкнутые нашим юзером фильмы, потом проверяем, если счетчик больше максимального количества схождений,
        значит, надо сохранить список нелайкнутых фильмов, так как это потенциальный результат
         */
        for (Map.Entry<Long, List<Long>> entry : likerToFilmsMap.entrySet()) {
            Integer count = 0;
            List<Long> notLikedFilmsId = new ArrayList<>();

            if (!entry.getKey().equals(id)) {
                for (Long filmId : entry.getValue()) {
                    if (likesOfUser.contains(filmId)) {
                        count++;
                    } else {
                        notLikedFilmsId.add(filmId);
                    }
                }
            }

            if (count > max) {
                result = notLikedFilmsId;
            }
        }

        return result;
    }

    private String selectName(User user) {
        return user.getName() != null && !user.getName().isEmpty() ? user.getName() : user.getLogin();
    }
}

