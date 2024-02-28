package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import ru.yandex.practicum.filmorate.exception.NotLikeException;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(min = 0, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private long duration;
    @Getter
    private static Long idCounter = 0L;
    @Getter
    private Set<Long> likerId = new HashSet<>();

    public static Long increaseIdCounter() {
        idCounter++;
        return idCounter;
    }

    public void addLiker(Long id) {
        likerId.add(id);
    }

    public void removeLiker(Long id) {
        if (!likerId.contains(id)) {
            throw new NotLikeException("Нельзя убрать лайк, так как вы его не поставили.");
        }
        likerId.remove(id);
    }

    public Integer getCountOfLikerId() {
        return likerId.size();
    }
}
