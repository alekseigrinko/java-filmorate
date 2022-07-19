package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaDbStorage mpaDbStorage;
    private final static Logger log = LoggerFactory.getLogger(MpaController.class);

    public MpaController(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    @GetMapping
    public List<Mpa> findAll() {
        log.debug("Получение списка всех MPA-рейтингов");
        return mpaDbStorage.findAllMpa();
    }


    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable long id) {
        log.debug("Запрос MPA-рейтинга ID: " + id);
        return mpaDbStorage.getMpaRating(id);
    }
}
