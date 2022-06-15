package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id){
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String putFriendByIdAndUserId(@PathVariable long id, @PathVariable long friendId){
        return userService.putFriendByIdAndUserId(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriendByIdAndUserId(@PathVariable long id, @PathVariable long friendId){
        return userService.deleteFriendByIdAndUserId(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriendsByUserId(@PathVariable long id){
        return userService.findFriendsByUserId(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriendsByFriendIdAndUserId(@PathVariable long id, @PathVariable long otherId){
        return userService.findCommonFriendsByFriendIdAndUserId(id, otherId);
    }
}