package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("Выведен список всех фильмов");
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Создание фильма " + film.getName());
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Обновление фильма " + film.getName());
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{id}/")
    public String deleteFilmById(@PathVariable long id) {
        log.debug("Удаление фильма ID " + id);
        return filmService.deleteFilmById(id);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.debug("Запрос фильма по ID: " + id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public String putLikeByFilmIdAndUserId(@PathVariable long id, @PathVariable long userId) {
        log.debug("Присвоение лайка фильму ID " + id + ", от пользователя ID: " + userId);
        return filmService.putLikeByFilmIdAndUserId(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String deleteLikeByFilmIdAndUserId(@PathVariable long id, @PathVariable long userId) {
        log.debug("Удаление лайка фильму ID " + id + ", от пользователя ID: " + userId);
        return filmService.deleteLikeByFilmIdAndUserId(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) final long count) {
        log.debug("Получение самых популярных фильмов");
        return filmService.getPopularFilms(count);
    }
}
