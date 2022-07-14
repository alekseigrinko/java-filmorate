package ru.yandex.practicum.filmorate.storage.filmgenres;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreStorage {
    List<FilmGenre> findAll();

    void create(long filmId, long genreId);

    void deleteFilmGenreByFilmId(long filmId);
}
