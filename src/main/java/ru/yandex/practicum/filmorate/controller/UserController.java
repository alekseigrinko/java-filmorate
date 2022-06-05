package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private int id = 0;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> users = new HashMap();

    @GetMapping
    public List<User> findAll() {
        List<User> usersList = new ArrayList<>();
        for (User user : users.values()) {
            usersList.add(user);
        }
        log.debug("Текущее количество пользователей: {}", usersList.size());
        return usersList;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        try {
            checkAndPut(user);
            id++;
            user.setId(id);
            users.put(user.getId(), user);
            log.debug("Сохранен пользователь: {}", user);
        } catch (ValidationException e) {
            log.warn("Произошла ошибка: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        try {
            if ((user.getId() > id) || (user.getId() <= 0)) {
                throw new ValidationException("Пользователя с таким ID (" + user.getId() + ") не зарегистрированно!"
                        + id);
            }
            checkAndPut(user);
            users.put(user.getId(), user);
            log.debug("Данные пользователя {} обновлены!", user);
        } catch (ValidationException e) {
            log.warn("Произошла ошибка: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            // проверка в Postman идет по коду 500 при обновлении пользователя
        }
        return user;
    }

    public void checkAndPut(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата дня рождения не наступила!");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}