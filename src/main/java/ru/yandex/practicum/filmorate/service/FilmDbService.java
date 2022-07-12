package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.filmlike.FilmLikeDbStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service("FilmDbService")
public class FilmDbService implements FilmServiceStorage {

    FilmDbStorage filmDbStorage;
    FilmLikeDbStorage filmLikeDbStorage;
    private final static Logger log = LoggerFactory.getLogger(FilmDbService.class);
    JdbcTemplate jdbcTemplate;

    public FilmDbService(FilmDbStorage filmDbStorage, FilmLikeDbStorage filmLikeDbStorage, JdbcTemplate jdbcTemplate) {
        this.filmDbStorage = filmDbStorage;
        this.filmLikeDbStorage = filmLikeDbStorage;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Film getFilmById(long id) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, filmDbStorage::makeFilm, id);
        if (films.size() != 1) {
            // TODO not found
        }
        return films.get(0);
    }

    @Override
    public String putLikeByFilmIdAndUserId(long id, long userId) {
        return "Лайк фильму поставлен: " + filmLikeDbStorage.create(id, userId);
    }

    @Override
    public String deleteLikeByFilmIdAndUserId(long id, long userId) {
        filmLikeDbStorage.deleteFilmLike(id, userId);
        return "Лайк фильму ID: " + id + " пользователя ID: " + userId + " удален";
    }

    @Override
    public List<Film> getPopularFilms(long count) {
        final String sqlQuery = "SELECT FILM_ID FROM FILM_LIKES " +
                "GROUP BY FILM_ID " +
                "ORDER BY COUNT(USER_ID) DESC " +
                "LIMIT ?";
        List<Film> films = jdbcTemplate.queryForList(sqlQuery, Long.class).stream()
                .map(this::getFilmById)
                .collect(Collectors.toList());
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        return filmDbStorage.create(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmDbStorage.update(film);
    }

    @Override
    public List<Film> findAll() {
        return filmDbStorage.findAll();
    }

}
