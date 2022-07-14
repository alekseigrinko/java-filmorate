package ru.yandex.practicum.filmorate.storage.filmlike;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.*;
import java.util.List;

@Repository
public class FilmLikeDbStorage implements FilmLikeStorage{

    JdbcTemplate jdbcTemplate;
    UserDbStorage userDbStorage;
    FilmDbStorage filmDbStorage;

    public FilmLikeDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage, FilmDbStorage filmDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
        this.filmDbStorage = filmDbStorage;
    }

    @Override
    public List<FilmLike> findAll() {
        final String sqlQuery = "SELECT * FROM FILM_LIKES";
        final List<FilmLike> filmLikes = jdbcTemplate.query(sqlQuery, this::makeFilmLike);
        return filmLikes;
    }

    @Override
    public FilmLike create(long filmId, long userId) {
        filmDbStorage.checkFilmId(filmId);
        userDbStorage.checkUserId(userId);
        String sqlQuery = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_LIKE_ID"});
            stmt.setLong(1, filmId);
            stmt.setLong(2, userId);
            return stmt;
        }, keyHolder);
        FilmLike filmLike = new FilmLike(keyHolder.getKey().longValue(), filmId, userId);
        return filmLike;
    }

    @Override
    public void deleteFilmLike(long filmId, long userId) {
        filmDbStorage.checkFilmId(filmId);
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

    public FilmLike makeFilmLike(ResultSet rs, int rowNum) throws SQLException {
        long filmLikeId = rs.getLong("FILM_LIKE_ID");
        long filmId = rs.getLong("FILM_ID");
        long userId = rs.getLong("USER_ID");
        return new FilmLike(filmLikeId, filmId, userId);
    }
}
