package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> findAll();

    Genre create(String name);

    void deleteGenre(long genreId);

    Genre getGenreById(long genreId);

    void addGenreByFilm(long filmId, long genreId);

    void deleteGenreByFilmByFilmId(long filmId);
}
