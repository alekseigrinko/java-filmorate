package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.filmlike.FilmLikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmLikeDbStorageTest {

    private final FilmLikeDbStorage filmLikeDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;

    private void createLike(){
        userDbStorage.create(new User(1,"mail@mail.ru", "dolore","Nick Name"
                , LocalDate.of(1946, 8, 20)));
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25)
                , 100);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, null));
        film.setGenres(genres);
        filmDbStorage.create(film);
        filmLikeDbStorage.create(1,1);
    }

    @Test
    @Order(1)
    void findAll() {
        createLike();
        List<Long> testList = filmLikeDbStorage.findAll();
        Assertions.assertEquals(1, filmLikeDbStorage.findAll().size());
        Assertions.assertEquals(testList.get(0), filmLikeDbStorage.findAll().get(0));
    }

    @Test
    void create() {
        createLike();
        Assertions.assertEquals(1, filmLikeDbStorage.findAll().get(0));
    }

    @Test
    void deleteFilmLike() {
        createLike();
        filmLikeDbStorage.deleteFilmLike(1,1);
        List<Long> testList = filmLikeDbStorage.findAll();
        Assertions.assertEquals(0, testList.size());
    }

    @Test
    void getPopularFilms() {
        createLike();
        List<Long> testList = filmLikeDbStorage.getPopularFilms(1);
        Assertions.assertEquals(1, testList.size());
    }
}