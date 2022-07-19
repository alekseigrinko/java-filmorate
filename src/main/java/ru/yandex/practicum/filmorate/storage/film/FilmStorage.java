package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {

    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    void deleteFilmById(long filmId);

    Film getFilmById(long id);

    Mpa getMpaRating(long mpaRatingId);

    List<Mpa> findAllMpa();

    List<Film> getPopularFilms(long count);

    String putLikeByFilmIdAndUserId(long id, long userId);

    String deleteLikeByFilmIdAndUserId(long id, long userId);

}
