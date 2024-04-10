package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.converter.ReviewDO2ReviewConverter;
import ru.yandex.practicum.filmorate.domain.ReviewDO;
import ru.yandex.practicum.filmorate.mapper.ReviewRowMapper;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReviewDbStorage implements ReviewStorage {

    private static final String ADD_REVIEW = "INSERT INTO review (user_id, film_id, content, isPositive, useful) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_REVIEW = "UPDATE review SET user_id=?, film_id=?, content=?, isPositive=?, useful=? WHERE id=?";
    private static final String DELETE_REVIEW = "DELETE FROM review WHERE id=?";
    private static final String GET_REVIEW_BY_ID = "SELECT * FROM review WHERE id=%s";
    public static final String ADD_LIKE = "INSERT INTO like (review_id, user_id) VALUES (?, ?)";
    public static final String REMOVE_LIKE = "DELETE FROM like WHERE review_id=?, user_id=?";
    public static final String ADD_DISLIKE = "INSERT INTO dislike (review_id, user_id) VALUES (?, ?)";
    public static final String REMOVE_DISLIKE = "DELETE FROM dislike WHERE review_id=?, user_id=?";

    private final JdbcTemplate jdbcTemplate;
    private final ReviewRowMapper reviewRowMapper;
    private final ReviewDO2ReviewConverter converter;
    private final RequestCreator requestCreator;


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

        jdbcTemplate.update(UPDATE_REVIEW,
                review.getUserId(),
                review.getFilmId(),
                review.getContent(),
                review.isPositive(),
                review.getUseful(),
                review.getReviewId());

        return getReviewFromStorage(review.getReviewId());

    }

    @Override
    public void deleteReview(int reviewId) {
        jdbcTemplate.update(DELETE_REVIEW, reviewId);
    }

    @Override
    public Optional<Review> getReviewById(Integer id) {
        String formattedSqlRequest = String.format(GET_REVIEW_BY_ID, id);
        try {
            ReviewDO reviewDO = jdbcTemplate.queryForObject(formattedSqlRequest, reviewRowMapper);
            if (reviewDO != null) {
                return Optional.of(converter.convert(reviewDO));
            }
        } catch (DataRetrievalFailureException exc) {
            log.warn("No object found by id={}", id, exc);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Review> getReviews(@Nullable Integer filmId, @Nullable Integer limit) {
        Collection<ReviewDO> reviewDOs = jdbcTemplate.queryForList(requestCreator.createRequest(filmId, limit), ReviewDO.class);
        return reviewDOs.stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void addLikeToReview(Integer reviewId, Integer userId) {
        jdbcTemplate.update(ADD_LIKE, reviewId, userId);
        jdbcTemplate.update(REMOVE_DISLIKE, reviewId, userId);
    }

    @Override
    public void addDislikeToReview(Integer reviewId, Integer userId) {
        jdbcTemplate.update(ADD_DISLIKE, reviewId, userId);
        jdbcTemplate.update(REMOVE_LIKE, reviewId, userId);
    }

    @Override
    public void removeLike(Integer reviewId, Integer userId) {
        jdbcTemplate.update(REMOVE_LIKE, reviewId, userId);
    }

    @Override
    public void removeDislike(Integer reviewId, Integer userId) {
        jdbcTemplate.update(REMOVE_DISLIKE, reviewId, userId);
    }
}
