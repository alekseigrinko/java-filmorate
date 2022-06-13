package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
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
    InMemoryFilmStorage inMemoryFilmStorage;
    @Getter
    FilmService filmService;


    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.update(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable("id") int id){
        return filmService.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public String putFriend(@PathVariable("id") int id, @PathVariable("userId") int userId){
        return filmService.putFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public String deleteFriend(@PathVariable("id") int id, @PathVariable("userId") int userId){
        return filmService.deleteFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) final int count){
        return filmService.getPopularFilms(count);
    }
}
