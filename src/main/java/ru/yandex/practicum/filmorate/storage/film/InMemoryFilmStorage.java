package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Getter
    private long id = 0;
    private final static Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    @Getter
    private final HashMap<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        List<Film> filmsList = new ArrayList<>();
        for (Film film : films.values()) {
            filmsList.add(film);
        }
        log.debug("Текущее количество добавленных фильмов: {}", filmsList.size());
        return filmsList;
    }

    @Override
    public Film create(Film film) {
        checkAndPut(film);
        id++;
        film.setId(id);
        films.put(film.getId(), film);
        log.debug("Фильм {} успешно добавлен в коллекцию", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if ((film.getId() > id) || (film.getId() <= 0)) {
            log.warn("Фильма с таким ID ( {} )не зарегистрировано!", film.getId());
            throw new ObjectNotFoundException("Фильма с таким ID (" + film.getId()
                    + ")не зарегистрировано!");
        }
        checkAndPut(film);
        films.put(film.getId(), film);
        log.debug("Данные фильма {} обновлены", film);
        return film;
    }

    @Override
    public void checkAndPut(Film film) {
        if (film.getDescription().length() > 200) {
            log.warn("Размер описания фильма превышает 200 символов!");
            throw new ValidationException("Размер описания фильма превышает 200 символов!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Некорректная дата релиза!");
            throw new ValidationException("Некорректная дата релиза!");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма не может быть меньше 0");
            throw new ValidationException("Продолжительность фильма не может быть меньше 0");
        }
    }
}
