package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
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
        return filmDbStorage.getFilmById(id);
    }

    @Override
    public String putLikeByFilmIdAndUserId(long id, long userId) {
        return "Поставлен лайк: " + filmLikeDbStorage.create(id, userId).toString();
    }

    @Override
    public String deleteLikeByFilmIdAndUserId(long id, long userId) {
        filmLikeDbStorage.deleteFilmLike(id, userId);
        return "Лайк фильму ID: " + id + ", пользователя ID: " + userId + " удален";
    }

    @Override
    public List<Film> getPopularFilms(long count) {
        List<FilmLike> likes = filmLikeDbStorage.findAll();
        if (likes.size() != 0) {
            List<Film> films = filmLikeDbStorage.getPopularFilms(count).stream()
                    .map(this::getFilmById)
                    .collect(Collectors.toList());
            /*findAll().stream().filter(film -> !films.contains(film)).forEach(films::add);*/
            return films;
        } else {
            return findAll();
        }
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
