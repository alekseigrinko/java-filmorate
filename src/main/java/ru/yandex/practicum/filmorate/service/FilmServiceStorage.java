package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceStorage {

    Film getFilmById(long id);

    String putLikeByFilmIdAndUserId(long id, long userId);

    String deleteLikeByFilmIdAndUserId(long id, long userId);

    void checkFilmById(long id);

    List<Film> getPopularFilms(long count);
}
