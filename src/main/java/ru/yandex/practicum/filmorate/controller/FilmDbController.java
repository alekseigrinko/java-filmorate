package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Qualifier
public class FilmDbController {

    private final FilmDbService filmDbService;
    private final static Logger log = LoggerFactory.getLogger(FilmDbController.class);


    public FilmDbController(FilmDbService filmDbService) {
        this.filmDbService = filmDbService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmDbService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmDbService.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmDbService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmDbService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public String putLikeByFilmIdAndUserId(@PathVariable long id, @PathVariable long userId) {
        return filmDbService.putLikeByFilmIdAndUserId(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String deleteLikeByFilmIdAndUserId(@PathVariable long id, @PathVariable long userId) {
        return filmDbService.deleteLikeByFilmIdAndUserId(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) final long count) {
        return filmDbService.getPopularFilms(count);
    }
}
