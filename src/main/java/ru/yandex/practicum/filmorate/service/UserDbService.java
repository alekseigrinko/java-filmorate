package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("UserDbService")
public class UserDbService implements UserServiceStorage{

    private final UserDbStorage userDbStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    private static final Logger log = LoggerFactory.getLogger(UserDbService.class);

    public UserDbService(UserDbStorage userDbStorage, FriendshipDbStorage friendshipDbStorage) {
        this.userDbStorage = userDbStorage;
        this.friendshipDbStorage = friendshipDbStorage;
    }


    @Override
    public User getUserById(long id) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, userDbStorage::makeUser,id);
        if (users.size() != 1) {
            // TODO not found
        }
        return users.get(0);
    }

    @Override
    public String putFriendByIdAndUserId(long id, long friendId) {
        return "Дружба пользователя " + id + " и пользователя " + friendId + " зарегистрирована по ID "
                + friendshipDbStorage.create(id, friendId) + " и направлена на подтверждение";
    }

    @Override
    public String deleteFriendByIdAndUserId(long id, long friendId) {
        friendshipDbStorage.deleteFriendship(id, friendId);
        return "Дружба пользователя " + id + " и " + friendId + " удалена";
    }

    @Override
    public List<User> findFriendsByUserId(long id){
        final String sqlQuery = "SELECT * FROM FRIENDSHIPS WHERE USER_ID=?";
        List<User> friends = jdbcTemplate.query(sqlQuery, friendshipDbStorage::makeFriendship, id).stream()
                .map(Friendship::getFriendId)
                .map(this::getUserById)
                .collect(Collectors.toList());
        return friends;
    }

    @Override
    public List<User> findCommonFriendsByFriendIdAndUserId(long id, long friendId) {
        final String sqlQuery = "SELECT * FROM FRIENDSHIPS WHERE USER_ID = ? AND USER_ID = ?";
        final List<Friendship> friendships = jdbcTemplate.query(sqlQuery, friendshipDbStorage::makeFriendship, id, friendId);
        List<User> friends = friendships.stream()
                .map(Friendship::getFriendId)
                .map(this::getUserById)
                .collect(Collectors.toList());
        return friends;
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
