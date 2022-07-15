package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service("UserDbService")
public class UserDbService implements UserServiceStorage{

    private final UserDbStorage userDbStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    public UserDbService(UserDbStorage userDbStorage, FriendshipDbStorage friendshipDbStorage) {
        this.userDbStorage = userDbStorage;
        this.friendshipDbStorage = friendshipDbStorage;
    }


    @Override
    public User getUserById(long id) {
        return userDbStorage.getUserById(id);
    }

    @Override
    public String putFriendByIdAndUserId(long id, long friendId) {
        return "Дружба пользователя " + id + " и пользователя " + friendId + " зарегистрирована по ID "
                + friendshipDbStorage.create(id, friendId) + " и направлена на подтверждение";
    }

    @Override
    public String deleteFriendByIdAndUserId(long id, long friendId) {
        friendshipDbStorage.deleteFriendshipByUserIdFriendId(id, friendId);
        return "Дружба пользователя " + id + " и " + friendId + " удалена";
    }

    @Override
    public List<User> findFriendsByUserId(long id){
        List<User> friends = friendshipDbStorage.getFriendshipByUserId(id).stream()
                .map(friendship -> getUserById(friendship.getFriendId()))
                .collect(Collectors.toList());
        System.out.println(friends);
        return friends;
    }

    @Override
    public List<User> findCommonFriendsByFriendIdAndUserId(long id, long friendId) {
        List<User> commonFriends = friendshipDbStorage.findCommonFriendsByFriendIdAndUserId(id, friendId).stream()
                .map(friendship -> getUserById(friendship.getFriendId()))
                .collect(Collectors.toList());
        return commonFriends;
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
    public List<User> findAll() {
        return userDbStorage.findAll();
    }

}
