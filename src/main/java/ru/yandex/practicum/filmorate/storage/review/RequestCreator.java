package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RequestCreator {

    private static final String GET_REVIEWS_ROOT = "SELECT * FROM review";
    private static final String DEFAULT_LIMIT = "10";

    public String createRequest(@Nullable Integer filmId, @Nullable Integer limit) {
        StringBuilder requestBuilder = new StringBuilder()
                .append(GET_REVIEWS_ROOT).append(" ");
        if(filmId != null) {
            requestBuilder.append(String.format("WHERE film_id=%s", filmId))
                    .append(" ");
        }
        requestBuilder.append(String.format("limit %s", getLimitValue(limit)));
        return requestBuilder.toString();
    }

    private String getLimitValue(@Nullable Integer limit) {
        if(limit != null) {
          return String.valueOf(limit);
        }
        return DEFAULT_LIMIT;
    }

}
