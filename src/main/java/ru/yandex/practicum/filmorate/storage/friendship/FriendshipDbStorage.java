package ru.yandex.practicum.filmorate.storage.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

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
    public List<Friendship> findAll() {
        final String sqlQuery = "SELECT * FROM FRIENDSHIPS";
        final List<Friendship> friendships = jdbcTemplate.query(sqlQuery, this::makeFriendship);
        return friendships;
    }

    @Override
    public long create(long userId, long friendId) {
        String sqlQuery = "INSERT INTO FRIENDSHIPS (USER_ID, FRIEND_ID, FRIENDSHIP_STATUSES_ID) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Friendship friendship = new Friendship(keyHolder.getKey().longValue(), userId, friendId
                , getFriendshipStatus("unconfirmed"));
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FRIENDSHIP_ID"});
            stmt.setLong(1, friendship.getUserId());
            stmt.setLong(2, friendship.getFriendId());
            stmt.setLong(3, friendship.getGetFriendshipStatusId());
            return stmt;
        }, keyHolder);
        return friendship.getFriendshipId();
    }

    @Override
    public String updateFriendshipStatus(Long friendship_id) {
        String sqlQuery = "UPDATE FRIENDSHIPS SET " +
                "FRIENDSHIP_STATUSES_ID = ?" +
                "WHERE FRIENDSHIP_ID = ?";
        jdbcTemplate.update(sqlQuery
                , getFriendshipStatus("confirmed")
                , friendship_id);
        return "Дружба ID " + friendship_id + " подтверждена";
    }

    public void deleteFriendship(long userId, long friendId) {
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

    public FriendshipStatus makeFriendshipStatus(ResultSet rs, int rowNum) throws SQLException {
        long friendshipStatusId = rs.getLong("FRIENDSHIP_STATUS_ID");
        String name = rs.getString("NAME");
        return new FriendshipStatus(friendshipStatusId, name);
    }

    Long getFriendshipStatus(String nameStatus) {
        String sqlQuery = "SELECT * FROM FRIENDSHIP_STATUSES WHERE NAME = ?";
        final List<FriendshipStatus> friendshipStatuses = jdbcTemplate.query(sqlQuery, this::makeFriendshipStatus, nameStatus);
        if (friendshipStatuses.size() != 1) {
            // TODO not found
        }
        return friendshipStatuses.get(0).getFriendShipStatusId();
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
}
