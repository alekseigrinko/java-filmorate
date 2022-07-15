package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    Film film;

    private final FilmDbStorage filmDbStorage;


    public void createFilm() {
        film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25)
                , 100);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, null));
        film.setGenres(genres);
        filmDbStorage.create(film);
    }

    public void createMpaAndGenre(Film film) {
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, null));
        film.setGenres(genres);
    }

    @Test
    public void testFindFilmById() {
        createFilm();
        System.out.println(filmDbStorage.findAll().toString());
        Film filmT = filmDbStorage.getFilmById(1);
        Assertions.assertEquals(filmT.getId(), 1);
    }

    @Test
    void updateFilm() {
        createFilm();
        film = new Film("domsuie isin", "gnicisipida", LocalDate.of(1967, 03, 25)
                , 100);
        film.setId(1);
        createMpaAndGenre(film);
        filmDbStorage.update(film);
        Assertions.assertEquals(filmDbStorage.getFilmById(1).getName(), "domsuie isin");

    }

    @Test
    void addFilmIncorrectDescription() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("nisi eiusmod", "DescriptionDescriptionDescriptionDescriptionDescription" +
                    "DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription" +
                    "DescriptionDescriptionDescriptionDescriptionDescriptionDDD", LocalDate.of(1967, 03
                    , 25), 100);
            createMpaAndGenre(film);
            filmDbStorage.create(film);
        });
        Assertions.assertEquals("Размер описания фильма превышает 200 символов!", thrown.getMessage());
    }

    @Test
    void addFilmIncorrectReleaseDate() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1895, 12
                    , 27), 100);
            film.setId(1);
            createMpaAndGenre(film);
            filmDbStorage.create(film);
        });
        Assertions.assertEquals("Некорректная дата релиза!", thrown.getMessage());
    }

    @Test
    void addFilmIncorrectDuration() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03,
                    25), -1);
            createMpaAndGenre(film);
            filmDbStorage.create(film);
        });
        Assertions.assertEquals("Продолжительность фильма не может быть меньше 0", thrown.getMessage());
    }

    @Test
    void addFilmIncorrectName() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("", "adipisicing", LocalDate.of(1967, 03,
                    25), 100);
            createMpaAndGenre(film);
            filmDbStorage.create(film);
        });
        Assertions.assertEquals("Название фильма не может быть пустым", thrown.getMessage());
    }

    @Test
    void updateFilmIncorrectId() {
        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            createFilm();
            film = new Film("domsuie isin", "gnicisipida", LocalDate.of(1967, 03, 25)
                    , 100);
            createMpaAndGenre(film);
            filmDbStorage.update(film);
        });
        Assertions.assertEquals("Фильма с ID " + film.getId() + " не найдено!", thrown.getMessage());
    }

    @Test
    void findAllFilms() {
        createFilm();
        List<Film> testList = filmDbStorage.findAll();
        Assertions.assertEquals(9, filmDbStorage.findAll().size());
        Assertions.assertEquals(testList.get(0), filmDbStorage.findAll().get(0));
    }

    @Test
    void findAllMpa() {
        List<Mpa> testList = filmDbStorage.findAllMpa();
        Assertions.assertEquals(5, filmDbStorage.findAllMpa().size());
    }

    @Test
    void getMpaById() {
        Assertions.assertEquals("G", filmDbStorage.getMpaRating(1).getName());
    }

}
