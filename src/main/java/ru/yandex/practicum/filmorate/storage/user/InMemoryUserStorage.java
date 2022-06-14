package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage{

    @Getter
    private int id = 0;
    @Getter
    private final HashMap<Integer, User> users = new HashMap();
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);

    @Override
    public List<User> findAll() {
        List<User> usersList = new ArrayList<>();
        for (User user : users.values()) {
            usersList.add(user);
        }
        log.debug("Текущее количество пользователей: {}", usersList.size());
        return usersList;
    }

    @Override
    public User create(User user) {
        checkAndPut(user);
        id++;
        user.setId(id);
        users.put(user.getId(), user);
        log.debug("Сохранен пользователь: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        if ((user.getId() > id) || (user.getId() <= 0)) {
            log.warn("Пользователя с таким ID ( {} ) не зарегистрировано!", user.getId());
            throw new ObjectNotFoundException("Пользователя с таким ID (" + user.getId() + ")" +
                    " не зарегистрировано!");
        }
        checkAndPut(user);
        users.put(user.getId(), user);
        log.debug("Данные пользователя {} обновлены!", user);
        return user;
    }

    @Override
    public void checkAndPut(User user) {
        if (user.getLogin().isEmpty() || (user.getLogin().contains(" "))) {
            log.warn("Ошибка написания логина!");
            throw new ValidationException("Ошибка написания логина!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата дня рождения не наступила!");
            throw new ValidationException("Дата дня рождения не наступила!");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}