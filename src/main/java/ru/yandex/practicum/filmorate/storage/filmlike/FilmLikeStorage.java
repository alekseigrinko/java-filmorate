package ru.yandex.practicum.filmorate.storage.filmlike;

import java.util.List;

public interface FilmLikeStorage {

    List<Long> findAll();

    void create(long filmId, long userId);

    void deleteFilmLike(long filmId, long userId);

    List<Long> getPopularFilms (long count);

}
