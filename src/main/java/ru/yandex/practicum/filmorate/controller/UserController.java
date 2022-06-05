package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    @Getter
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
        checkAndPut(user);
        id++;
        user.setId(id);
        users.put(user.getId(), user);
        log.debug("Сохранен пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if ((user.getId() > id) || (user.getId() <= 0)) {
            log.warn("Пользователя с таким ID ( {} ) не зарегистрировано!", user.getId());
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Пользователя с таким ID (" + user.getId() + ")" +
                    " не зарегистрировано!");
        }
        checkAndPut(user);
        users.put(user.getId(), user);
        log.debug("Данные пользователя {} обновлены!", user);
        return user;
    }

    public void checkAndPut(User user) {
        if (user.getLogin().isEmpty() || (user.getLogin().contains(" "))) {
            log.warn("Ошибка написания логина!");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Ошибка написания логина!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата дня рождения не наступила!");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата дня рождения не наступила!");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}