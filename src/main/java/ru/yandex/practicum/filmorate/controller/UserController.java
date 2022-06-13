package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    @Getter
    private InMemoryUserStorage inMemoryUserStorage;

    @Getter
    private UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return inMemoryUserStorage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return inMemoryUserStorage.update(user);
    }

    @GetMapping("/users/{id}")
    public User getFilm(@PathVariable("id") int id){
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public String putFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId){
        return userService.putFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId){
        return userService.deleteFriend(id, friendId);
    }

    @PutMapping("/users/{id}/friends")
    public Set<Integer> findFriends(@PathVariable("id") int id){
        return userService.findFriends(id);
    }

    @PutMapping("/users/{id}/friends/common/{otherId}")
    public Set<Integer> findAllFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId){
        return userService.findAllFriends(id, otherId);
    }
}