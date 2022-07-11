package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDbService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Qualifier
public class UserDbController {

    private final UserDbService userDbService;
    private final static Logger log = LoggerFactory.getLogger(UserDbController.class);


    public UserDbController(UserDbService userDbService) {
        this.userDbService = userDbService;
    }

    @GetMapping
    public List<User> findAll() {
        return userDbService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userDbService.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userDbService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userDbService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String putFriendByIdAndUserId(@PathVariable long id, @PathVariable long friendId) {
        return userDbService.putFriendByIdAndUserId(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriendByIdAndUserId(@PathVariable long id, @PathVariable long friendId) {
        return userDbService.deleteFriendByIdAndUserId(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriendsByUserId(@PathVariable long id) {
        return userDbService.findFriendsByUserId(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriendsByFriendIdAndUserId(@PathVariable long id, @PathVariable long otherId) {
        return userDbService.findCommonFriendsByFriendIdAndUserId(id, otherId);
    }
}