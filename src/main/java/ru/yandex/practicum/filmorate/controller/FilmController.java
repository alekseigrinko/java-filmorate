package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmServiceStorage filmServiceStorage;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    public FilmController(@Qualifier("FilmDbService") FilmServiceStorage filmServiceStorage) {
        this.filmServiceStorage = filmServiceStorage;
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("Выведен список всех фильмов");
        return filmServiceStorage.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Создание фильма " + film.getName());
        return filmServiceStorage.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Обновление фильма " + film.getName());
        return filmServiceStorage.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.debug("Запрос фильма по ID: " + id);
        return filmServiceStorage.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public String putLikeByFilmIdAndUserId(@PathVariable long id, @PathVariable long userId) {
        log.debug("Присвоение лайка фильму ID " + id + ", от пользователя ID: " + userId);
        return filmServiceStorage.putLikeByFilmIdAndUserId(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String deleteLikeByFilmIdAndUserId(@PathVariable long id, @PathVariable long userId) {
        log.debug("Удаление лайка фильму ID " + id + ", от пользователя ID: " + userId);
        return filmServiceStorage.deleteLikeByFilmIdAndUserId(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) final long count) {
        log.debug("Получение самых популярных фильмов");
        return filmServiceStorage.getPopularFilms(count);
    }
}
