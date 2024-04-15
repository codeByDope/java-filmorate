package ru.yandex.practicum.filmorate.storage.film;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PopularFilmsRequestCreator {

    private static final String MOST_POPULAR_FILMS_REQUEST_BASIS = "SELECT f.*\n" +
            "FROM films AS f\n" +
            "INNER JOIN (\n" +
            "    SELECT film_id, COUNT(liker_id) AS count_likes\n" +
            "    FROM likers\n" +
            "    GROUP BY film_id\n" +
            "    ORDER BY count_likes DESC\n" +
            "    LIMIT %s\n" +
            ") AS top_films ON f.id = top_films.film_id" +
            " INNER JOIN films_genres fg ON f.id = fg.film_id";
    private static final String AND_QUERY_STATEMENT = " AND ";
    private static final String WHERE_QUERY_STATEMENT = " WHERE ";
    private static final String YEAR_QUERY_CONDITION = "EXTRACT(YEAR FROM release_date)=%d";
    private static final String GENRE_ID_CONDITION = "fg.genre_id=%d";


    public String createMostPopularFilmsQueryByFilmAndGenre(Long count,
                                                            @NonNull Integer genreId,
                                                            @NonNull Integer year) {
        return new StringBuilder()
                .append(String.format(MOST_POPULAR_FILMS_REQUEST_BASIS, count))
                .append(WHERE_QUERY_STATEMENT)
                .append(String.format(YEAR_QUERY_CONDITION, year))
                .append(AND_QUERY_STATEMENT)
                .append(String.format(GENRE_ID_CONDITION, genreId))
                .toString();
    }

    public String createMostPopularFilmsQuery(Long count) {
        return String.format(MOST_POPULAR_FILMS_REQUEST_BASIS, count);
    }

    public String createMostPopularFilmsQueryByGenreId(Long count,
                                                       @NonNull Integer genreId) {
        return new StringBuilder()
                .append(String.format(MOST_POPULAR_FILMS_REQUEST_BASIS, count))
                .append(WHERE_QUERY_STATEMENT)
                .append(String.format(GENRE_ID_CONDITION, genreId))
                .toString();
    }

    public String createMostPopularFilmsQueryByYear(Long count,
                                                    @NonNull Integer year) {
        return new StringBuilder()
                .append(String.format(MOST_POPULAR_FILMS_REQUEST_BASIS, count))
                .append(WHERE_QUERY_STATEMENT)
                .append(String.format(YEAR_QUERY_CONDITION, year))
                .toString();
    }
}
