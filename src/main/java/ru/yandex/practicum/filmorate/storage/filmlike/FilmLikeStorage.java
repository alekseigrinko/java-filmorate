package ru.yandex.practicum.filmorate.storage.filmlike;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmLikeStorage {
    List<FilmLike> findAll();

    FilmLike create(long filmId, long userId);

    void deleteFilmLike(long filmId, long userId);
}
