package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmServiceStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final FilmDbStorage filmDbStorage;
    private final static Logger log = LoggerFactory.getLogger(MpaController.class);

    public MpaController(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    @GetMapping
    public List<Mpa> findAll() {
        return filmDbStorage.findAllMpa();
    }


    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable long id) {
        return filmDbStorage.getMpaRating(id);
    }
}
