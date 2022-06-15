package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserServiceStorage{

    @Getter
    InMemoryUserStorage inMemoryUserStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public User getUserById(long id) {
        checkUserById(id);
        log.debug("Предоставлена информация по пользователю {}", inMemoryUserStorage.getUsers().get(id));
        return inMemoryUserStorage.getUsers().get(id);
    }

    @Override
    public String putFriendByIdAndUserId(long id, long friendId) {
        checkUserById(id);
        checkUserById(friendId);
        if (inMemoryUserStorage.getUsers().get(id).getFriends().contains(friendId)) {
            log.debug("Пользователь ID:" + friendId + " уже является другом");
            return ("Пользователь ID:" + friendId + " уже является другом");
        }
        inMemoryUserStorage.getUsers().get(id).getFriends().add(friendId);
        inMemoryUserStorage.getUsers().get(friendId).getFriends().add(id);
        log.debug("Добавлен пользователю " +  inMemoryUserStorage.getUsers().get(id).getName()
                + " новый друг с ID:" + friendId);
        return ("Добавлен пользователю " +  inMemoryUserStorage.getUsers().get(id).getName()
                + " новый друг с ID:" + friendId);
    }

    @Override
    public String deleteFriendByIdAndUserId(long id, long friendId) {
        checkUserById(id);
        checkUserById(friendId);
        if (!inMemoryUserStorage.getUsers().get(id).getFriends().contains(friendId)) {
            log.warn("Пользователь с ID: " + friendId + " не был другом "
                    + inMemoryUserStorage.getUsers().get(id).getName());
            throw new ObjectNotFoundException("Пользователь с ID: " + friendId + " не был другом "
                    + inMemoryUserStorage.getUsers().get(id).getName());
        }
        for (int i = 0; i < inMemoryUserStorage.getUsers().get(id).getFriends().size(); i++) {
            if (inMemoryUserStorage.getUsers().get(id).getFriends().get(i) == friendId) {
                inMemoryUserStorage.getUsers().get(id).getFriends().remove(i);
            }
        }
        for (int i = 0; i < inMemoryUserStorage.getUsers().get(friendId).getFriends().size(); i++) {
            if (inMemoryUserStorage.getUsers().get(friendId).getFriends().get(i) == id) {
                inMemoryUserStorage.getUsers().get(friendId).getFriends().remove(i);
            }
        }
        log.debug("Пользователь " +  inMemoryUserStorage.getUsers().get(id).getName()
                + " больше не дружит с ID:" + friendId);
        return ("Пользователь " +  inMemoryUserStorage.getUsers().get(id).getName()
                + " больше не дружит с ID:" + friendId);
    }

    @Override
    public void checkUserById(long id) {
        if ((inMemoryUserStorage.getId() < id) || (id <= 0)) {
            log.warn("Пользователя с таким ID ( {} )не зарегистрировано!", id);
            throw new ObjectNotFoundException("Пользователя с таким ID (" + id
                    + ")не зарегистрировано!");
        }
    }

    @Override
    public List<User> findFriendsByUserId(long id){
        checkUserById(id);
        List<User> friends = new ArrayList<>();
        for (long i : inMemoryUserStorage.getUsers().get(id).getFriends()) {
            friends.add(inMemoryUserStorage.getUsers().get(i));
        }
        log.debug("Предоставлен список друзей пользователя {}", inMemoryUserStorage.getUsers().get(id));
        return friends;
    }

    @Override
    public List<User> findCommonFriendsByFriendIdAndUserId(long id, long friendId) {
        checkUserById(id);
        checkUserById(friendId);
        List<Long> commonFriends = new ArrayList<>();
        for (long check : inMemoryUserStorage.getUsers().get(id).getFriends()) {
            if (inMemoryUserStorage.getUsers().get(friendId).getFriends().contains(check)) {
                commonFriends.add(check);
            }
        }
        List<User> friends = new ArrayList<>();
        for (long i : commonFriends) {
            friends.add(inMemoryUserStorage.getUsers().get(i));
        }
        log.debug("Предоставлен общий список друзей пользователя " + inMemoryUserStorage.getUsers().get(id)
                + " и пользователя " + inMemoryUserStorage.getUsers().get(friendId));
        return friends;
    }

    public User createUser(User user) {
        return inMemoryUserStorage.create(user);
    }

    public User updateUser(User user) {
        return inMemoryUserStorage.update(user);
    }

    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }
}
