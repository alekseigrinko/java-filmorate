package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.List;

@Service("FilmDbService")
public class FilmDbService implements FilmServiceStorage {

    FilmDbStorage filmDbStorage;
    JdbcTemplate jdbcTemplate;

    public FilmDbService(FilmDbStorage filmDbStorage, JdbcTemplate jdbcTemplate) {
        this.filmDbStorage = filmDbStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilmById(long id) {
        return filmDbStorage.getFilmById(id);
    }

    @Override
    public String putLikeByFilmIdAndUserId(long id, long userId) {
        return filmDbStorage.putLikeByFilmIdAndUserId(id, userId);
    }

    @Override
    public String deleteLikeByFilmIdAndUserId(long id, long userId) {
        return filmDbStorage.deleteLikeByFilmIdAndUserId(id, userId);
    }

    @Override
    public List<Film> getPopularFilms(long count) {
        return filmDbStorage.getPopularFilms(count);
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
