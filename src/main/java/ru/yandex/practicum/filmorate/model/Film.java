package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.validation.ValidReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Film {
    private Long id;
    @NotNull(message = "Название фильма не существует")
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Размер описания не должен превышать 200 символов")
    private String description;
    @ValidReleaseDate
    @NotNull(message = "Дата выхода не существует")
    private LocalDate releaseDate;
    @Positive(message = "Длительность фильма не может быть отрицательной")
    private long duration;
    private List<Genre> genres = new ArrayList<>();
    private MpaRating mpa;

    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private LocalDate releaseDate;
        private long duration;
        private List<Genre> genres;
        private MpaRating mpa;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder releaseDate(LocalDate releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder duration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder genres(List<Genre> genres) {
            this.genres = genres;
            return this;
        }

        public Builder mpa(MpaRating mpa) {
            this.mpa = mpa;
            return this;
        }

        public Film build() {
            Film film = new Film();
            film.id = this.id;
            film.name = this.name;
            film.description = this.description;
            film.releaseDate = this.releaseDate;
            film.duration = this.duration;
            film.genres = this.genres;
            film.mpa = this.mpa;
            return film;
        }
    }
}
