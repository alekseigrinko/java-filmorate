package ru.yandex.practicum.filmorate.storage.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;

import java.sql.*;
import java.util.List;

@Repository
public class FriendshipDbStorage implements FriendshipStorage {

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
        String sqlQuery = "INSERT INTO FRIENDSHIPS (USER_ID, FRIEND_ID, FRIENDSHIP_STATUS_ID) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FRIENDSHIP_ID"});
            stmt.setLong(1, userId);
            stmt.setLong(2, friendId);
            stmt.setLong(3, getFriendshipStatus("unconfirmed"));
            return stmt;
        }, keyHolder);
    }

    @Override
    public String updateFriendshipStatus(Long friendship_id) {
        checkFriendshipId(friendship_id);
        String sqlQuery = "UPDATE FRIENDSHIPS SET " +
                "FRIENDSHIP_STATUS_ID = ?" +
                "WHERE FRIENDSHIP_ID = ?";
        jdbcTemplate.update(sqlQuery
                , getFriendshipStatus("confirmed")
                , friendship_id);
        return "Дружба ID " + friendship_id + " подтверждена";
    }

    @Override
    public void deleteFriendshipByUserIdFriendId(long userId, long friendId) {
        String sqlQuery = "DELETE FROM FRIENDSHIPS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }


    public Long getFriendshipStatus(String nameStatus) {
        String sqlQuery = "SELECT FRIENDSHIP_STATUS_ID FROM FRIENDSHIP_STATUSES WHERE NAME = ?";
        final List<Long> friendshipStatuses = jdbcTemplate.queryForList(sqlQuery, Long.class, nameStatus);
        if (friendshipStatuses.size() != 1) {
            // TODO not found
        }
        return friendshipStatuses.get(0);
    }

    private void createFriendshipStatus(String nameStatus) {
        String sqlQuery = "INSERT INTO FRIENDSHIP_STATUSES (NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FRIENDSHIP_STATUS_ID"});
            stmt.setString(1, nameStatus);
            return stmt;
        }, keyHolder);
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

    public void checkFriendshipId(long id) {
        String sqlQuery = "SELECT FRIENDSHIP_STATUS_ID FROM FRIENDSHIPS WHERE FRIENDSHIP_ID = ?";
        final List<Long> users = jdbcTemplate.queryForList(sqlQuery, Long.class, id);
        if (users.size() != 1) {
            log.warn("Дружбы с ID " + id + " не найдено!");
            throw new ObjectNotFoundException("Дружбы с ID " + id + " не найдено!");
        }
    }
}
