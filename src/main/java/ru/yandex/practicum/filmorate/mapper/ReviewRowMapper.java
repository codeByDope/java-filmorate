package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
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
    private static final String USEFUL = "useful";

    @Override
    public ReviewDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ReviewDO.builder()
                .reviewId(rs.getInt(ID))
                .filmId(rs.getInt(FILM_ID))
                .userId(rs.getInt(USER_ID))
                .content(rs.getString(CONTENT))
                .isPositive(rs.getBoolean(IS_POSITIVE))
                .useful(rs.getInt(USEFUL))
                .build();
    }
}
