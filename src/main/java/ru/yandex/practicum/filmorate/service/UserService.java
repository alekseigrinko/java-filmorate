package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    InMemoryUserStorage inMemoryUserStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User getUser(int id) {
        checkUser(id);
        return inMemoryUserStorage.getUsers().get(id);
    }

    public String putFriend(int id, int friendId) {
        checkUser(id);
        checkUser(friendId);
        inMemoryUserStorage.getUsers().get(id).getFriends().add(friendId);
        return ("Добавлен пользователю " +  inMemoryUserStorage.getUsers().get(id).getName()
                + " новый друг с ID:" + friendId);
    }

    public String deleteFriend(int id, int friendId) {
        checkUser(id);
        checkUser(friendId);
        if (!inMemoryUserStorage.getUsers().get(id).getFriends().contains(friendId)) {
            throw new ObjectNotFoundException("Пользователь с ID: " + friendId + " не был другом "
                    + inMemoryUserStorage.getUsers().get(id).getName());
        }
        inMemoryUserStorage.getUsers().get(id).getFriends().remove(friendId);
        return ("Пользователь " +  inMemoryUserStorage.getUsers().get(id).getName()
                + " больше не дружит с ID:" + friendId);
    }

    private void checkUser(int id) {
        if ((inMemoryUserStorage.getId() > id) || (inMemoryUserStorage.getId() <= 0)) {
            log.warn("Пользователя с таким ID ( {} )не зарегистрировано!", id);
            throw new ObjectNotFoundException("Пользователя с таким ID (" + id
                    + ")не зарегистрировано!");
        }
    }

    public Set<Integer> findFriends(int id){
        checkUser(id);
        return inMemoryUserStorage.getUsers().get(id).getFriends();
    }

    public Set<Integer> findAllFriends(int id, int friendId) {
        checkUser(id);
        checkUser(friendId);
        Set<Integer> commonFriends = new HashSet<>();
        for (int check : inMemoryUserStorage.getUsers().get(id).getFriends()) {
            if (inMemoryUserStorage.getUsers().get(friendId).getFriends().contains(check)) {
                commonFriends.add(check);
            }
        }
        return commonFriends;
    }
}
