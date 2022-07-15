package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.FilmServiceStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.filmgenres.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.filmlike.FilmLikeDbStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {
    private static Film film;
    private static FilmController filmController;
    private FilmServiceStorage filmServiceStorage;


    @BeforeAll
    public void SetUp() {
        film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25)
                ,  100);
        filmController = new FilmController(filmServiceStorage);
    }

    @Test
    void addFilm() {
        Film testFilm = filmController.create(film);
        film.setId(testFilm.getId());
        Assertions.assertEquals(filmController.getFilmById(film.getId()), film);
        Assertions.assertEquals(film, testFilm);
    }

    @Test
    void updateFilm() {
        Film testFilm = filmController.create(film);
        film = new Film("domsuie isin", "gnicisipida", LocalDate.of(1967, 03, 25)
                ,  100);
        film.setId(testFilm.getId());
        filmController.update(film);
        Assertions.assertEquals(filmController.getFilmById(film.getId()), film);

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
        Assertions.assertEquals("Размер описания фильма превышает 200 символов!", thrown.getMessage());
    }

    @Test
    void addFilmIncorrectReleaseDate() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1895, 12
                    , 27) , 100);
            filmController.create(film);
        });
        Assertions.assertEquals("Некорректная дата релиза!", thrown.getMessage());
    }

    @Test
    void addFilmIncorrectDuration() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03,
                    25), -1);
            filmController.create(film);
        });
        Assertions.assertEquals("Продолжительность фильма не может быть меньше 0", thrown.getMessage());
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
        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            filmController.create(film);
            film = new Film("domsuie isin", "gnicisipida", LocalDate.of(1967, 03, 25)
                    ,  100);
            film.setId(-1);
            filmController.update(film);
        });
        Assertions.assertEquals("Фильма с таким ID (" + film.getId() + ")не зарегистрировано!", thrown.getMessage());
    }

    @Test
    void findAll() {
        filmController.create(film);
        List<Film> testList = filmController.findAll();
        Assertions.assertEquals(1, filmController.findAll().size());
        Assertions.assertEquals(testList.get(0), filmController.findAll().get(0));
    }

    @Test
    void getFilm(){
        filmController.create(film);
        Film testFilm = filmController.getFilmById(film.getId());
        Assertions.assertEquals(film, testFilm);
    }

    @Test
    void checkLikes() {
        Film testFilm = new Film("test", "description", LocalDate.of(1967, 03, 25)
                ,  100);
        Film testFilm2 = new Film("test2", "noitpircsed", LocalDate.of(1967, 03, 25)
                ,  100);
        filmController.create(film);
        filmController.create(testFilm);
        filmController.create(testFilm2);
        filmController.putLikeByFilmIdAndUserId(film.getId(),1);
        filmController.putLikeByFilmIdAndUserId(film.getId(),2);
        filmController.putLikeByFilmIdAndUserId(film.getId(),3);
/*
        Assertions.assertEquals(3, film.getLikes().size());
*/
        filmController.putLikeByFilmIdAndUserId(testFilm.getId(),1);
        filmController.putLikeByFilmIdAndUserId(testFilm.getId(),2);
        filmController.putLikeByFilmIdAndUserId(testFilm2.getId(),1);
        List<Film> popularFilms = filmController.getPopularFilms(1);
        Assertions.assertEquals(film, popularFilms.get(0));
        filmController.deleteLikeByFilmIdAndUserId(testFilm2.getId(),1);
/*
        Assertions.assertTrue(testFilm2.getLikes().isEmpty());
*/
    }
}
