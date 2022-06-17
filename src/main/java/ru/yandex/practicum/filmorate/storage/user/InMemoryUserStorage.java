package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage{

    @Getter
    private long id = 0;
    @Getter
    private final HashMap<Long, User> users = new HashMap();
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);

    @Override
    public List<User> findAll() {
        List<User> usersList = new ArrayList<>(users.values());
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
