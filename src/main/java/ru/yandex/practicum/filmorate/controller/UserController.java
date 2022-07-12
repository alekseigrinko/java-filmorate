package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
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
        return userServiceStorage.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userServiceStorage.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        /*checkUserById(user.getId());*/
        return userServiceStorage.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id){
        /*checkUserById(id);*/
        return userServiceStorage.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String putFriendByIdAndUserId(@PathVariable long id, @PathVariable long friendId){
        /*checkUserById(id);
        checkUserById(friendId);*/
        return userServiceStorage.putFriendByIdAndUserId(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriendByIdAndUserId(@PathVariable long id, @PathVariable long friendId){
        /*checkUserById(id);
        checkUserById(friendId);*/
        return userServiceStorage.deleteFriendByIdAndUserId(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriendsByUserId(@PathVariable long id){
        /*checkUserById(id);*/
        return userServiceStorage.findFriendsByUserId(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriendsByFriendIdAndUserId(@PathVariable long id, @PathVariable long otherId){
        return userServiceStorage.findCommonFriendsByFriendIdAndUserId(id, otherId);
    }

    /*public boolean isExistById(long id) {
        if ((userService.getId() < id) || (id <= 0)) {
            return true;
        } else {
            return false;
        }

    }

    public void checkUserById(long id) {
        if (isExistById(id)) {
            log.warn("Пользователя с таким ID ( {} )не зарегистрировано!", id);
            throw new ObjectNotFoundException("Пользователя с таким ID (" + id
                    + ") не зарегистрировано!");
        }
    }*/
}