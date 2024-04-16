package ru.yandex.practicum.filmorate.storage.film;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PopularFilmsRequestCreator {

    private static final String MOST_POPULAR_FILMS_REQUEST_BASIS = "SELECT f.* " +
            "FROM films AS f " +
            "LEFT JOIN films_genres fg ON f.id = fg.film_id " +
            "LEFT JOIN (" +
            "SELECT film_id, COUNT(liker_id) AS count_likes " +
            "FROM likers " +
            "GROUP BY film_id " +
            "ORDER BY count_likes DESC " +
            ") AS top_films ON f.id = top_films.film_id ";
    private static final String AND_QUERY_STATEMENT = " AND ";
    private static final String WHERE_QUERY_STATEMENT = " WHERE ";
    private static final String LIMIT_QUERY_STATEMENT = " LIMIT %s ";
    private static final String YEAR_QUERY_CONDITION = "EXTRACT(YEAR FROM release_date)=%d ";
    private static final String GENRE_ID_CONDITION = "fg.genre_id=%d ";


    public String createMostPopularFilmsQueryByFilmAndGenre(Long count,
                                                            @NonNull Integer genreId,
                                                            @NonNull Integer year) {
        return new StringBuilder()
                .append(MOST_POPULAR_FILMS_REQUEST_BASIS)
                .append(WHERE_QUERY_STATEMENT)
                .append(String.format(YEAR_QUERY_CONDITION, year))
                .append(AND_QUERY_STATEMENT)
                .append(String.format(GENRE_ID_CONDITION, genreId))
                .append(String.format(LIMIT_QUERY_STATEMENT, count))
                .toString();
    }

    public String createMostPopularFilmsQuery(Long count) {
        return new StringBuilder()
                .append(MOST_POPULAR_FILMS_REQUEST_BASIS)
                .append(String.format(LIMIT_QUERY_STATEMENT, count))
                .toString();

    }

    public String createMostPopularFilmsQueryByGenreId(Long count,
                                                       @NonNull Integer genreId) {
        return new StringBuilder()
                .append(MOST_POPULAR_FILMS_REQUEST_BASIS)
                .append(WHERE_QUERY_STATEMENT)
                .append(String.format(GENRE_ID_CONDITION, genreId))
                .append(String.format(LIMIT_QUERY_STATEMENT, count))
                .toString();
    }

    public String createMostPopularFilmsQueryByYear(Long count,
                                                    @NonNull Integer year) {
        return new StringBuilder()
                .append(MOST_POPULAR_FILMS_REQUEST_BASIS)
                .append(WHERE_QUERY_STATEMENT)
                .append(String.format(YEAR_QUERY_CONDITION, year))
                .append(String.format(LIMIT_QUERY_STATEMENT, count))
                .toString();
    }
}
