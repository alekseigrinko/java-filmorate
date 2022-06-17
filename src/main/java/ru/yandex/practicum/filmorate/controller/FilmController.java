package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
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
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        checkFilmById(film.getId());
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        checkFilmById(id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public String putLikeByFilmIdAndUserId(@PathVariable long id, @PathVariable long userId) {
        checkFilmById(id);
        return filmService.putLikeByFilmIdAndUserId(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String deleteLikeByFilmIdAndUserId(@PathVariable long id, @PathVariable long userId) {
        checkFilmById(id);
        return filmService.deleteLikeByFilmIdAndUserId(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) final long count) {
        return filmService.getPopularFilms(count);
    }

    private boolean isExistById(long id) {
        if ((filmService.getId() < id) || (id <= 0)) {
            return true;
        } else {
            return false;
        }
    }

    private void checkFilmById(long id) {
        if (isExistById(id)) {
            log.warn("Фильма с таким ID ( {} )не зарегистрировано!", id);
            throw new ObjectNotFoundException("Фильма с таким ID (" + id
                    + ")не зарегистрировано!");
        }
    }
}
