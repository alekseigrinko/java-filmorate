package ru.yandex.practicum.filmorate.storage.filmlike;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Component
public class FilmLikeDbStorage implements FilmLikeStorage{

    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Override
    public List<FilmLike> findAll() {
        final String sqlQuery = "SELECT * FROM FILM_LIKES";
        final List<FilmLike> filmLikes = jdbcTemplate.query(sqlQuery, this::makeFilmLike);
        return filmLikes;
    }

    @Override
    public FilmLike create(long filmId, long userId) {
        String sqlQuery = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        FilmLike filmLike = new FilmLike(keyHolder.getKey().longValue(), filmId, userId);
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FRIENDSHIP_ID"});
            stmt.setLong(1, filmLike.getFilmId());
            stmt.setLong(2, filmLike.getUserId());
            return stmt;
        }, keyHolder);
        return filmLike;
    }

    @Override
    public void deleteFilmLike(long filmId, long userId) {
        String sqlQuery = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public FilmLike makeFilmLike(ResultSet rs, int rowNum) throws SQLException {
        long filmLikeId = rs.getLong("FILM_LIKE_ID");
        long filmId = rs.getLong("FILM_ID");
        long userId = rs.getLong("USER_ID");
        return new FilmLike(filmLikeId, filmId, userId);
    }
}
