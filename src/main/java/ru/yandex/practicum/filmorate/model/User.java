package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import ru.yandex.practicum.filmorate.exception.NotFriendException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    @Getter
    private static Long idCounter = 0L;
    @Getter
    private Set<Long> friendsId = new HashSet<>();

    public static Long increaseIdCounter() {
        idCounter++;
        return idCounter;
    }

    public void addFriend(Long id) {
        if (id >= 0) {
            friendsId.add(id);
            return;
        }
        throw new UserNotFoundException("Пользователь не может иметь отрицательный ID!");
    }

    public void removeFriend(Long id) {
        if (!friendsId.contains(id)) {
            throw new NotFriendException("Нельзя удалить из друзей пользователя, не являющегося вашим другом!");
        }
        friendsId.remove(id);
    }


}
