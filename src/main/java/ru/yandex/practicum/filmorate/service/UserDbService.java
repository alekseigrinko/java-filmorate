package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;

@Service("UserDbService")
public class UserDbService implements UserService {

    private final UserDbStorage userDbStorage;

    public UserDbService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    @Override
    public User getUserById(long id) {
        return userDbStorage.getUserById(id);
    }

    @Override
    public String putFriendByIdAndUserId(long id, long friendId) {
        return userDbStorage.putFriendByIdAndUserId(id, friendId);
    }

    @Override
    public String deleteFriendByIdAndUserId(long id, long friendId) {
        userDbStorage.deleteFriendByIdAndUserId(id, friendId);
        return "Дружба пользователя " + id + " и " + friendId + " удалена";
    }

    @Override
    public List<User> findFriendsByUserId(long id){
        return userDbStorage.findFriendsByUserId(id);
    }

    @Override
    public List<User> findCommonFriendsByFriendIdAndUserId(long id, long friendId) {
        return userDbStorage.findCommonFriendsByFriendIdAndUserId(id, friendId);
    }

    @Override
    public User createUser(User user) {
        return userDbStorage.create(user);
    }

    @Override
    public User updateUser(User user) {
        return userDbStorage.update(user);
    }

    @Override
    public String deleteUserById(long userId) {
       userDbStorage.deleteUserById(userId);
       return "Удаление пользователя ID: " + userId;
    }

    @Override
    public List<User> findAll() {
        return userDbStorage.findAll();
    }

}
