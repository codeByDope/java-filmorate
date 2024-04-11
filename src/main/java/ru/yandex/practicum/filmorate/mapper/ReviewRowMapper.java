package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.domain.ReviewDO;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewRowMapper implements RowMapper<ReviewDO> {

    private static final String ID = "id";
    private static final String FILM_ID = "film_id";
    private static final String USER_ID = "user_id";
    private static final String CONTENT = "content";
    private static final String IS_POSITIVE = "is_positive";
    private static final String LIKES_NUMBER = "like_count";
    private static final String DISLIKES_NUMBER = "dislike_count";

    @Override
    public ReviewDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ReviewDO.builder()
                .reviewId(rs.getLong(ID))
                .filmId(rs.getLong(FILM_ID))
                .userId(rs.getLong(USER_ID))
                .content(rs.getString(CONTENT))
                .isPositive(rs.getBoolean(IS_POSITIVE))
                .likesNumber(rs.getInt(LIKES_NUMBER))
                .dislikesNumber(rs.getInt(DISLIKES_NUMBER))
                .build();
    }

    public ReviewDO mapRowSet(SqlRowSet rowSet) {
        return ReviewDO.builder()
                .reviewId(rowSet.getLong(ID))
                .filmId(rowSet.getLong(FILM_ID))
                .userId(rowSet.getLong(USER_ID))
                .content(rowSet.getString(CONTENT))
                .isPositive(rowSet.getBoolean(IS_POSITIVE))
                .likesNumber(rowSet.getInt(LIKES_NUMBER))
                .dislikesNumber(rowSet.getInt(DISLIKES_NUMBER))
                .build();
    }
}
