package ru.yandex.practicum.filmorate.storage.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.*;
import java.util.List;

@Repository
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(FriendshipDbStorage.class);

private UserDbStorage userDbStorage;
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }


    @Override
    public List<Friendship> findAll() {
        final String sqlQuery = "SELECT * FROM FRIENDSHIPS";
        final List<Friendship> friendships = jdbcTemplate.query(sqlQuery, this::makeFriendship);
        return friendships;
    }

    @Override
    public long create(long userId, long friendId) {
        userDbStorage.checkUserId(userId);
        userDbStorage.checkUserId(friendId);
        String sqlQuery = "INSERT INTO FRIENDSHIPS (USER_ID, FRIEND_ID, FRIENDSHIP_STATUS_ID) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FRIENDSHIP_ID"});
            stmt.setLong(1, userId);
            stmt.setLong(2, friendId);
            stmt.setLong(3, getFriendshipStatus("unconfirmed"));
            return stmt;
        }, keyHolder);
        Friendship friendship = new Friendship(keyHolder.getKey().longValue(), userId, friendId
                , getFriendshipStatus("unconfirmed"));
        return friendship.getFriendshipId();
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
        userDbStorage.checkUserId(userId);
        userDbStorage.checkUserId(friendId);
        String sqlQuery = "DELETE FROM FRIENDSHIPS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public Friendship makeFriendship(ResultSet rs, int rowNum) throws SQLException {
        long friendshipId = rs.getLong("USER_ID");
        long userId = rs.getLong("USER_ID");
        long friendId = rs.getLong("FRIEND_ID");
        long friendshipStatusId = rs.getLong("FRIENDSHIP_STATUS_ID");
        return new Friendship(friendshipId, userId, friendId, friendshipStatusId);
    }

    public Long getFriendshipStatus(String nameStatus) {
        String sqlQuery = "SELECT * FROM FRIENDSHIP_STATUSES WHERE NAME = ?";
        final List<FriendshipStatus> friendshipStatuses = jdbcTemplate.query(sqlQuery, this::makeFriendshipStatus, nameStatus);
        if (friendshipStatuses.size() != 1) {
            // TODO not found
        }
        return friendshipStatuses.get(0).getFriendShipStatusId();
    }

    public FriendshipStatus makeFriendshipStatus(ResultSet rs, int rowNum) throws SQLException {
        long friendshipStatusId = rs.getLong("FRIENDSHIP_STATUS_ID");
        String name = rs.getString("NAME");
        return new FriendshipStatus(friendshipStatusId, name);
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
    public List<Friendship> getFriendshipByUserId (long id) {
        userDbStorage.checkUserId(id);
        final String sqlQuery = "SELECT * FROM FRIENDSHIPS WHERE USER_ID=?";
        List<Friendship> friends = jdbcTemplate.query(sqlQuery, this::makeFriendship, id);
        return friends;
    }

    @Override
    public List<Friendship> findCommonFriendsByFriendIdAndUserId(long id, long friendId) {
        userDbStorage.checkUserId(id);
        userDbStorage.checkUserId(friendId);
        final String sqlQuery =
                "SELECT u.* FROM FRIENDSHIPS u, FRIENDSHIPS f  " +
                        "WHERE u.FRIEND_ID = f.FRIEND_ID AND u.USER_ID = ? AND f.USER_ID = ?";
        final List<Friendship> friendships = jdbcTemplate.query(sqlQuery, this::makeFriendship, id, friendId);
        return friendships;
    }

    public void checkFriendshipId(long id) {
        String sqlQuery = "SELECT * FROM FRIENDSHIPS WHERE FRIENDSHIP_ID = ?";
        final List<Friendship> users = jdbcTemplate.query(sqlQuery, this::makeFriendship, id);
        if (users.size() != 1) {
            log.warn("Дружбы с ID " + id + " не найдено!");
            throw new ObjectNotFoundException("Дружбы с ID " + id + " не найдено!");
        }
    }
}
