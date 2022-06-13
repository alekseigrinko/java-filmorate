package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    InMemoryFilmStorage inMemoryFilmStorage;
    private final static Logger log = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public Film getFilm(int id) {
        checkFilm(id);
        return inMemoryFilmStorage.getFilms().get(id);
    }

    public String putFilm(int id, int userId) {
        checkFilm(id);
        checkFilm(userId);
        inMemoryFilmStorage.getFilms().get(id).getLikes().add(userId);
        return ("Фильму " +  inMemoryFilmStorage.getFilms().get(id).getName()
                + " поставил лайк пользователь с ID:" + userId);
    }

    public String deleteFilm(int id, int userId) {
        checkFilm(id);
        checkFilm(userId);
        if (!inMemoryFilmStorage.getFilms().get(id).getLikes().contains(userId)) {
            throw new ObjectNotFoundException("Пользователь с ID: " + userId + "еще не ставил лайк фильму "
                    + inMemoryFilmStorage.getFilms().get(id).getName());
        }
        inMemoryFilmStorage.getFilms().get(id).getLikes().remove(userId);
        return ("Пользователю ID: " + userId + " больше не нравится фильм "
                + inMemoryFilmStorage.getFilms().get(id).getName());
    }

    private void checkFilm(int id) {
        if ((inMemoryFilmStorage.getId() > id) || (inMemoryFilmStorage.getId() <= 0)) {
            log.warn("Фильма с таким ID ( {} )не зарегистрировано!", id);
            throw new ObjectNotFoundException("Фильм с таким ID (" + id
                    + ")не зарегистрировано!");
        }
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> checkList = new ArrayList<>();
        for (Film film : inMemoryFilmStorage.getFilms().values()) {
            checkList.add(film);
        }
        return checkList.stream()
                .sorted((f0, f1) -> compare(f0, f1))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        int result = f0.getLikes().size() - f1.getLikes().size();
        return result;
    }
}
