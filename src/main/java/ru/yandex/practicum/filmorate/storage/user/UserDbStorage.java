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

    @Override
    public User add(User user) {
        if (user.getId() == null) {
            return addWithGeneratedId(user);
        } else {
            return addWithSpecifiedId(user);
        }
    }

    private User addWithGeneratedId(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName() != null && !user.getName().isEmpty() ? user.getName() : user.getLogin());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        user.setName(user.getName() != null && !user.getName().isEmpty() ? user.getName() : user.getLogin()); // Обновляем имя пользователя на логин, если оно пустое
        return user;
    }

    private User addWithSpecifiedId(User user) {
        String sql = "INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, user.getId());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getLogin());
            ps.setString(4, user.getName() != null && !user.getName().isEmpty() ? user.getName() : user.getLogin());
            ps.setDate(5, Date.valueOf(user.getBirthday()));
            return ps;
        });

        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?;";

        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(),
                Date.valueOf(user.getBirthday()), user.getId());

        return user;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users;";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public Optional<User> getById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?;";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);

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
        String getListOfLikerSql = "SELECT * FROM likers;";
        Map<Long, List<Long>> likerToFilmsMap = new HashMap<>();
        Integer max = 0;
        List<Long> result = new ArrayList<>();

        //Заполняем likersToFilmsMap: Ключ - id лайкнувшего, Значение - список лайкнутых фильмов
        SqlRowSet rs = jdbcTemplate.queryForRowSet(getListOfLikerSql);
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

        //Получаю список лайков пользователя, которому подбираем рекомендации
        List<Long> likesOfUser = likerToFilmsMap.get(id);
        /*Перебираем остальных юзеров, считаем количество сходств и одновременно сохраняем
        нелайкнутые нашим юзером фильмы, потом проверяем, если счетчик больше максимального количества схождений,
        значит, надо сохранить список нелайкнутых фильмов, так как это потеницальный результат
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
}
