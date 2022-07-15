package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceStorage userServiceStorage;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);


    public UserController(@Qualifier("UserDbService") UserServiceStorage userServiceStorage) {
        this.userServiceStorage = userServiceStorage;
    }

    @GetMapping
    public List<User> findAll() {
        log.debug("Получение всех пользователей");
        return userServiceStorage.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Регистрация пользователя " + user.getName());
        return userServiceStorage.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Обновление данных пользователя " + user.getName());
        return userServiceStorage.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id){
        log.debug("Запрос пользователя с ID: " + id);
        return userServiceStorage.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String putFriendByIdAndUserId(@PathVariable long id, @PathVariable long friendId){
        log.debug("Запрос дружбы пользователя ID " + id + ", пользователю ID: " + friendId);
        return userServiceStorage.putFriendByIdAndUserId(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriendByIdAndUserId(@PathVariable long id, @PathVariable long friendId){
        log.debug("Удаление дружбы пользователя ID " + id + ", и пользователя ID: " + friendId);
        return userServiceStorage.deleteFriendByIdAndUserId(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriendsByUserId(@PathVariable long id){
        log.debug("Получение списка всех друзей пользователя ID " + id);
        return userServiceStorage.findFriendsByUserId(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriendsByFriendIdAndUserId(@PathVariable long id, @PathVariable long otherId){
        log.debug("Получение списка общих друзей пользователя ID " + id + ", и пользователя ID: " + otherId);
        return userServiceStorage.findCommonFriendsByFriendIdAndUserId(id, otherId);
    }
}