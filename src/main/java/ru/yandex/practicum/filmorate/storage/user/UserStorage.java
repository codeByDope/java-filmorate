package ru.yandex.practicum.filmorate.storage.user;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User add(User user);

    User update(User user);

    void delete(Long id);

    List<User> getAll();

    Optional<User> getById(Long id);

    List<Long> getRecommendations(Long id);
}
