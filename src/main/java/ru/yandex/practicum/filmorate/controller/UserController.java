package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Getter
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.getInMemoryUserStorage().findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.getInMemoryUserStorage().create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.getInMemoryUserStorage().update(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id){
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String putFriend(@PathVariable long id, @PathVariable long friendId){
        return userService.putFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable long id, @PathVariable long friendId){
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable long id){
        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findAllFriends(@PathVariable long id, @PathVariable long otherId){
        return userService.findAllFriends(id, otherId);
    }
}