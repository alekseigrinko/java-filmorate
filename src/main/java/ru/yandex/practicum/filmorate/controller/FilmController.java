package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    @Getter
    FilmService filmService;


    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.getInMemoryFilmStorage().findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.getInMemoryFilmStorage().create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.getInMemoryFilmStorage().update(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public String putFriend(@PathVariable int id, @PathVariable int userId) {
        return filmService.putFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String deleteFriend(@PathVariable int id, @PathVariable int userId) {
        return filmService.deleteFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) final int count) {
        return filmService.getPopularFilms(count);
    }
}
