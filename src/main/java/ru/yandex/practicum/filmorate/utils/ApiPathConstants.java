package ru.yandex.practicum.filmorate.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPathConstants {
    public static final String DIRECTORS_PATH = "/directors";
    public static final String BY_ID_PATH = "/{id}";
    public static final String REVIEW_PATH = "reviews";
    public static final String LIKE_PATH = BY_ID_PATH + "/like/{userId}";
    public static final String DISLIKE_PATH = BY_ID_PATH + "/dislike/{userId}";

}