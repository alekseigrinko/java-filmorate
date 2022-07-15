package ru.yandex.practicum.filmorate.storage.filmlike;

import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.List;

public interface FilmLikeStorage {
    List<FilmLike> findAll();

    FilmLike create(long filmId, long userId);

    void deleteFilmLike(long filmId, long userId);

    List<Long> getPopularFilms (long count);

}
