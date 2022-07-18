package ru.yandex.practicum.filmorate.storage.filmlike;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.*;
import java.util.List;

@Repository
public class FilmLikeDbStorage implements FilmLikeStorage{

    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    public FilmLikeDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public List<Long> findAll() {
        final String sqlQuery = "SELECT USER_ID FROM FILM_LIKES";
        final List<Long> filmLikes = jdbcTemplate.queryForList(sqlQuery, Long.class);
        return filmLikes;
    }

    @Override
    public void create(long filmId, long userId) {
        userDbStorage.checkUserId(userId);
        String sqlQuery = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_LIKE_ID"});
            stmt.setLong(1, filmId);
            stmt.setLong(2, userId);
            return stmt;
        }, keyHolder);
    }

    @Override
    public void deleteFilmLike(long filmId, long userId) {
        userDbStorage.checkUserId(userId);
        String sqlQuery = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Long> getPopularFilms (long count) {
        final String sqlQuery = "SELECT FILM_ID FROM FILM_LIKES " +
                "GROUP BY FILM_ID " +
                "ORDER BY COUNT(USER_ID) DESC " +
                "LIMIT ?";
        List<Long> popularFilmsId = jdbcTemplate.queryForList(sqlQuery, Long.class, count);
        System.out.println(popularFilmsId);
        return popularFilmsId;
    }

    public List<Long> getLikesByIdFilm (long filmId) {
        String sqlQuery = "SELECT USER_ID FROM FILM_LIKES WHERE FILM_ID = ?";
        final List<Long> filmLikes = jdbcTemplate.queryForList(sqlQuery, Long.class, filmId);
        if (filmLikes.size() != 1) {
            // TODO not found
        }
        return filmLikes;
    }

}
