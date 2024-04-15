package ru.yandex.practicum.filmorate.controller.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPathConstants {

    public static final String REVIEW_PATH = "reviews";
    public static final String BY_ID_PATH = "/{id}";
    public static final String LIKE_PATH = BY_ID_PATH + "/like/{userId}";
    public static final String DISLIKE_PATH = BY_ID_PATH + "/dislike/{userId}";
    public static final String DIRECTORS_PATH = "/directors";
    public static final String FILM_BY_DIRECTOR_PATH = "/director/{directorId}";
    public static final String POPULAR_FILMS_PATH = "/popular";
    public static final String FILM_PATH = "/films";
}
