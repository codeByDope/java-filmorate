package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public User addUser(User user);
    public User updateUser(User user);
    public User deleteUser(Integer id);
    public Collection<User> getUsers();
    public User getUserById(Long id);
}
