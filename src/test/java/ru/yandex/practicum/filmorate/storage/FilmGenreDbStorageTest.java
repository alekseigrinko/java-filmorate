package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.filmgenres.FilmGenreDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmGenreDbStorageTest {

    private final FilmGenreDbStorage filmGenreDbStorage;
    private final FilmDbStorage filmDbStorage;

    public void createFilm() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25)
                , 100);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, null));
        film.setGenres(genres);
        filmDbStorage.create(film);
    }

    @Test
    void findAll() {
        createFilm();
        List<FilmGenre> testList = filmGenreDbStorage.findAll();
        Assertions.assertEquals(1, testList.size());
        Assertions.assertEquals(testList.get(0).getGenreId(), filmGenreDbStorage.getGenreByFilmId(1).get(0));
    }
}