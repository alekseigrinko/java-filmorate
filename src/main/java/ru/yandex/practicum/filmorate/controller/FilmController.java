package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private int id = 0;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    @Getter
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        List<Film> filmsList = new ArrayList<>();
        for (Film film : films.values()) {
            filmsList.add(film);
        }
        log.debug("Текущее количество добавленных фильмов: {}", filmsList.size());
        return filmsList;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        checkAndPut(film);
        id++;
        film.setId(id);
        films.put(film.getId(), film);
        log.debug("Фильм {} успешно добавлен в коллекцию", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if ((film.getId() > id) || (film.getId() <= 0)) {
            log.warn("Фильма с таким ID ( {} )не зарегистрировано!", film.getId());
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Фильма с таким ID (" + film.getId()
                    + ")не зарегистрировано!");
        }
        checkAndPut(film);
        films.put(film.getId(), film);
        log.debug("Данные фильма {} обновлены", film);
        return film;
    }

    public void checkAndPut(Film film) {
        if (film.getDescription().length() > 200) {
            log.warn("Размер описания фильма превышает 200 символов!");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Размер описания фильма превышает 200 символов!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Некорректная дата релиза!");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Некорректная дата релиза!");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма не может быть меньше 0");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Продолжительность фильма не может быть меньше 0");
        }
    }
}
