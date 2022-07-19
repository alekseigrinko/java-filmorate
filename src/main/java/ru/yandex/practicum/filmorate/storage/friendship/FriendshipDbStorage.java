package ru.yandex.practicum.filmorate.storage.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import java.util.List;

@Repository
public class FriendshipDbStorage implements FriendshipStorage{
    private final JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(FriendshipDbStorage.class);

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Long> findAll() {
        final String sqlQuery = "SELECT FRIEND_ID FROM FRIENDSHIPS";
        final List<Long> friendships = jdbcTemplate.queryForList(sqlQuery, Long.class);
        return friendships;
    }

    @Override
    public void create(long userId, long friendId) {
        if (checkFriendship(userId, friendId)) {
            String sqlQuery = "INSERT INTO FRIENDSHIPS (USER_ID, FRIEND_ID) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, userId, friendId);
        }
    }


    @Override
    public void deleteFriendshipByUserIdFriendId(long userId, long friendId) {
        String sqlQuery = "DELETE FROM FRIENDSHIPS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }


    @Override
    public List<Long> getFriendByUserId (long id) {
        final String sqlQuery = "SELECT FRIEND_ID FROM FRIENDSHIPS WHERE USER_ID=?";
        List<Long> friends = jdbcTemplate.queryForList(sqlQuery, Long.class, id);
        return friends;
    }

    @Override
    public List<Long> findCommonFriendsByFriendIdAndUserId(long id, long friendId) {
        final String sqlQuery =
                "SELECT u.FRIEND_ID FROM FRIENDSHIPS u, FRIENDSHIPS f  " +
                        "WHERE u.FRIEND_ID = f.FRIEND_ID AND u.USER_ID = ? AND f.USER_ID = ?";
        final List<Long> friendships = jdbcTemplate.queryForList(sqlQuery, Long.class, id, friendId);
        return friendships;
    }

    public boolean checkFriendship(long userId, long friendId) {
        String sqlQuery = "SELECT USER_ID FROM FRIENDSHIPS WHERE USER_ID = ? AND FRIEND_ID = ?";
        final List<Long> friendships = jdbcTemplate.queryForList(sqlQuery, Long.class, userId, friendId);
        if (friendships.size() == 1) {
            log.warn("Дружба уже зарегистрирована!");
            return false;
        }
        return true;
    }
}
