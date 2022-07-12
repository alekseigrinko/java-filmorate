package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceStorage {

    Film getFilmById(long id);

    String putLikeByFilmIdAndUserId(long id, long userId);

    String deleteLikeByFilmIdAndUserId(long id, long userId);

    List<Film> getPopularFilms(long count);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> findAll();
}
