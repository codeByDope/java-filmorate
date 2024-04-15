package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RequestCreator {

    private static final String DEFAULT_LIMIT = "10";

    public String createRequest(@Nullable Integer filmId, @Nullable Integer limit) {
        StringBuilder requestBuilder = new StringBuilder()
                .append("SELECT r.*, COALESCE(l.like_count, 0) AS like_count, COALESCE(d.dislike_count, 0) AS dislike_count " +
                        "FROM review r " +
                        "LEFT JOIN (SELECT review_id, COUNT(DISTINCT user_id) AS like_count FROM likes GROUP BY review_id) l ON r.id = l.review_id " +
                        "LEFT JOIN (SELECT review_id, COUNT(DISTINCT user_id) AS dislike_count FROM dislikes GROUP BY review_id) d ON r.id = d.review_id\n"
                );
        if (filmId != null) {
            requestBuilder.append(String.format("WHERE r.film_id=%s ", filmId));
        }
        requestBuilder.append(String.format("limit %s", getLimitValue(limit)));
        return requestBuilder.toString();
    }

    private String getLimitValue(@Nullable Integer limit) {
        if (limit != null) {
            return String.valueOf(limit);
        }
        return DEFAULT_LIMIT;
    }

}
