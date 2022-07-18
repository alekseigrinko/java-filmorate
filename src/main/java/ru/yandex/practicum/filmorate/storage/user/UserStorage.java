package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    User create (User user);

    User update(User user);

    User getUserById (long id);

    String putFriendByIdAndUserId(long id, long friendId);

    String deleteFriendByIdAndUserId(long id, long friendId);

    List<User> findFriendsByUserId(long id);

    List<User> findCommonFriendsByFriendIdAndUserId(long id, long friendId);
}
