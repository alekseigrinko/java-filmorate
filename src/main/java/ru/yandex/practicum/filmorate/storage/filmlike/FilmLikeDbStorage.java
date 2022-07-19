package ru.yandex.practicum.filmorate.storage.filmlike;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;

@Repository
public class FilmLikeDbStorage implements FilmLikeStorage{

    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;
    private static final Logger log = LoggerFactory.getLogger(FriendshipDbStorage.class);

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
        if (checkLike(filmId, userId)) {
            userDbStorage.checkUserId(userId);
            String sqlQuery = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, filmId, userId);
        }
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

    public boolean checkLike(long filmId, long userId) {
        String sqlQuery = "SELECT FILM_ID FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        final List<Long> likes = jdbcTemplate.queryForList(sqlQuery, Long.class, filmId, userId);
        if (likes.size() == 1) {
            log.warn("Лайк уже зарегистрирован!");
            /*throw new ValidationException("Лайк уже зарегистрирован!");*/
            return false;
        }
        return true;
    }

}
