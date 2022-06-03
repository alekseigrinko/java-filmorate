package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    @Setter
    @Getter
    private int id = 0;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        List<Film> filmsList = new ArrayList<>();
        for (Film film: films.values()) {
            filmsList.add(film);
        }
        log.debug("Текущее количество добавленных фильмов: {}", filmsList.size());
        return filmsList;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        try {
            if (film.getName().isEmpty()) {
                throw new ValidationException("Не указано название фильма!");
            } else if (film.getDescription().length() > 200) {
                throw new ValidationException("Размер описания фильма превышает 200 символов!");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
                throw new ValidationException("Некорректная дата релиза!");
            } else if (film.getDuration() < 0) {
                throw new ValidationException("Продолжительность фильма не может быть меньше 0");
            } else {
                setId(getId() + 1);
                film.setId(getId());
                films.put(film.getId(), film);
                log.debug("Данные фильма {} обновлены", film);
            }
        } catch (ValidationException e) {
            log.warn("Произошла ошибка: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        try {
            if ((film.getId() > getId()) || (film.getId() <= 0)) {
                throw new ValidationException("Фильма с таким ID не зарегистрированно! " + getId());
            }
            if (film.getName().isEmpty() || film.getName().isBlank()) {
                throw new ValidationException("Не указано название фильма!");
            } else if (film.getDescription().length() > 200) {
                throw new ValidationException("Размер описания фильма превышает 200 символов!");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
                throw new ValidationException("Некорректная дата релиза!");
            } else if (film.getDuration() < 0) {
                throw new ValidationException("Продолжительность фильма не может быть меньше 0");
            } else {
                films.put(film.getId(), film);
                log.debug("Данные фильма {} обновлены", film);
            }
        } catch (ValidationException e) {
            log.warn("Произошла ошибка: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return film;
    }
}
