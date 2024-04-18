package ru.yandex.practicum.filmorate.controller.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPathConstants {
    public static final String SEARCH_FILMS_PATH = "/search";
    public static final String REVIEW_PATH = "reviews";
    public static final String BY_ID_PATH = "/{id}";
    public static final String LIKE_PATH = BY_ID_PATH + "/like/{userId}";
    public static final String DISLIKE_PATH = BY_ID_PATH + "/dislike/{userId}";
    public static final String DIRECTORS_PATH = "/directors";
    public static final String FILM_BY_DIRECTOR_PATH = "/director/{directorId}";
    public static final String POPULAR_FILMS_PATH = "/popular";
    public static final String FILM_PATH = "/films";
    public static final String USER_PATH = "/users";
    public static final String FRIENDS_PATH_BY_ID_AND_FRIEND_ID = "/{id}/friends/{friendId}";
    public static final String FRIENDS_PATH_BY_ID = "/{id}/friends";
    public static final String COMMON_FRIENDS_PATH = "/{id}/friends/common/{otherId}";
    public static final String GENRE_PATH = "/genres";
    public static final String MPA_PATH = "/mpa";
    public static final String RECOMMENDATIONS_PATH = "/{id}/recommendations";
    public static final String COMMON_PATH = "/common";
}
