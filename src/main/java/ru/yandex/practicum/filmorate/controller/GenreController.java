package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreDbStorage genreDbStorage;
    private final static Logger log = LoggerFactory.getLogger(GenreController.class);

    public GenreController(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    @GetMapping
    public List<Genre> findAll() {
        log.debug("Получение списка всех жанров");
        return genreDbStorage.findAll();
    }


    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable long id) {
        log.debug("Запрос жанра ID: " + id);
        return genreDbStorage.getGenreById(id);
    }
}
