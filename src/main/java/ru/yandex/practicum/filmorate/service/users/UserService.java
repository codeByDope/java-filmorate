package ru.yandex.practicum.filmorate.service.users;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User getUserById(Long id);

    User addUser(User user);

    User updateUser(User user);
  
    void delete(Long id);
  
    List<Film> getRecommendations(Long id);
}
