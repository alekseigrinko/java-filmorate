package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private static Film film;
    private static FilmController filmController;

    @BeforeEach
    public void SetUp() {
        film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25)
                ,  100);
        filmController = new FilmController();
    }

    @Test
    void addFilm() {
        Film testFilm = filmController.create(film);
        film.setId(testFilm.getId());
        Assertions.assertTrue(filmController.getFilms().containsValue(film));
        Assertions.assertEquals(film, testFilm);
    }

    @Test
    void updateFilm() {
        Film testFilm = filmController.create(film);
        film = new Film("domsuie isin", "gnicisipida", LocalDate.of(1967, 03, 25)
                ,  100);
        film.setId(testFilm.getId());
        filmController.update(film);
        Assertions.assertTrue(filmController.getFilms().containsValue(film));
        Assertions.assertEquals(filmController.getFilms().get(film.getId()).getName(),"domsuie isin");
    }

    @Test
    void addFilmIncorrectDescription() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("nisi eiusmod", "DescriptionDescriptionDescriptionDescriptionDescription" +
                    "DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription" +
                    "DescriptionDescriptionDescriptionDescriptionDescriptionDDD", LocalDate.of(1967, 03
                    , 25), 100);
            filmController.create(film);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void addFilmIncorrectReleaseDate() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1895, 12
                    , 27) , 100);
            filmController.create(film);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void addFilmIncorrectDuration() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03,
                    25), -1);
            filmController.create(film);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void addFilmIncorrectName() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        film = new Film(null, "adipisicing", LocalDate.of(1967, 03,
                    25), 100);
        Set<ConstraintViolation<Film>> violations = validator.validate(filmController.create(film));
        System.out.println(violations);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void updateFilmIncorrectId() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            filmController.create(film);
            film = new Film("domsuie isin", "gnicisipida", LocalDate.of(1967, 03, 25)
                    ,  100);
            film.setId(-1);
            filmController.update(film);
        });
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }

    @Test
    void findAll() {
        filmController.create(film);
        List<Film> testList = filmController.findAll();
        Assertions.assertEquals(1, filmController.getFilms().size());
        Assertions.assertEquals(testList.get(0), filmController.getFilms().get(film.getId()));
    }
}
