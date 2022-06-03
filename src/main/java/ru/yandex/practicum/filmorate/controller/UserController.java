package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Getter
    @Setter
    private int id = 0;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> users = new HashMap();

    @GetMapping
    public List<User> findAll() {
        List<User> usersList = new ArrayList<>();
        for (User user: users.values()) {
            usersList.add(user);
        }
        log.debug("Текущее количество пользователей: {}", usersList.size());
        return usersList;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        try {
            if ((user.getEmail().isEmpty()) || (!user.getEmail().contains("@"))) {
                throw new ValidationException("Значение адреса электронной почты пустое или не содержит символ @ !");
            } else if ((user.getLogin().isEmpty()) && (user.getLogin().contains(" "))) {
                throw new ValidationException("Логин пустой либо содержит пробелы!");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата дня рождения не наступила!");
            } else {
                if (user.getName().isEmpty()) {
                    user.setName(user.getLogin());
                }
                setId(getId() + 1);
                user.setId(getId());
                users.put(user.getId(), user);
                log.debug("Сохранен пользователь: {}", user);
            }
        } catch (ValidationException e) {
            log.warn("Произошла ошибка: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        try {
            if ((user.getId() > getId()) || (user.getId() <= 0)) {
                throw new ValidationException("Пользователя с таким ID (" + user.getId() + ") не зарегистрированно!" + getId());
            }
            if ((user.getEmail().isEmpty()) || (!user.getEmail().contains("@"))) {
                throw new ValidationException("Значение адреса электронной почты пустое или не содержит символ @ !");
            } else if ((user.getLogin().isEmpty()) && (user.getLogin().contains(" "))) {
                throw new ValidationException("Логин пустой либо содержит пробелы!");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата дня рождения не наступила!");
            } else {
                if (user.getName().isEmpty()) {
                    user.setName(user.getLogin());
                }
                users.put(user.getId(), user);
                log.debug("Данные пользователя {} обновлены!", user);
            }
        } catch (ValidationException e) {
            log.warn("Произошла ошибка: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return user;
    }
}