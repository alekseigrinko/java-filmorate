package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.TreeSet;

public interface UserServiceStorage {

    User getUserById(long id);

    String putFriendByIdAndUserId(long id, long friendId);

    String deleteFriendByIdAndUserId(long id, long friendId);

    List<User> findFriendsByUserId(long id);

    List<User> findCommonFriendsByFriendIdAndUserId(long id, long friendId);

    User createUser(User user);

    User updateUser(User user);

    List<User> findAll();
}
