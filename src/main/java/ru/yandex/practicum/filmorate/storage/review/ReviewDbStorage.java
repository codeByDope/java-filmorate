package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class ReviewDbStorage implements ReviewStorage {

    private static final String ADD_REVIEW = "INSERT INTO review (userId, filmId, content, isPositive, useful) VALUES (?, ?, ?, ?, ?)";
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Review addReview(Review review) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(ADD_REVIEW, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getFilmId());
            ps.setString(3, review.getContent());
            ps.setBoolean(4, review.isPositive());
            ps.setInt(5, review.getUseful());
            return ps;
        }, keyHolder);
        Integer generatedReviewId = (Integer) keyHolder.getKey();

        return getReviewFromStorage(generatedReviewId);
    }

    @Override
    public Review updateReview(Review review) {
        return null;
    }

    @Override
    public void deleteReview(Review review) {

    }

    @Override
    public Optional<Review> getReviewById(Integer id) {
        return null;
    }

    @Override
    public Collection<Review> getReviews(Integer filmId, Integer limit) {
        return null;
    }
}
