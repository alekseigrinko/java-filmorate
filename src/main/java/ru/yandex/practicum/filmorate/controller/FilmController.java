package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
        try {
            checkAndPut(film);
            id++;
            film.setId(id);
            films.put(film.getId(), film);
            log.debug("Фильм {} успешно добавлен в коллекцию", film);
        } catch (ValidationException e) {
            log.warn("Произошла ошибка: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        try {
            if ((film.getId() > id) || (film.getId() <= 0)) {
                throw new ValidationException("Фильма с таким ID (" + film.getId() + ")не зарегистрированно!");
            }
            checkAndPut(film);
            films.put(film.getId(), film);
            log.debug("Данные фильма {} обновлены", film);
        } catch (ValidationException e) {
            log.warn("Произошла ошибка: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            // проверка в Postman идет по коду 500 при обновлении фильма

        }
        return film;
    }

    public void checkAndPut(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Размер описания фильма превышает 200 символов!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некорректная дата релиза!");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть меньше 0");
        }
    }
}
