package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Repository("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<User> findAll() {
        final String sqlQuery = "SELECT * FROM USERS";
        final List<User> users = jdbcTemplate.query(sqlQuery, this::makeUser);
        return users;
    }

    public User makeUser(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("NAME");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();
        return new User(id, email, login, name, birthday);
    }

    @Override
    public User create(User user) {
        checkUser(user);
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        checkUserId(user.getId());
        checkUser(user);
        String sqlQuery = "UPDATE USERS SET " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId()
        );
        return user;
    }

    @Override
    public User getUserById (long id) {
        checkUserId(id);
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, this::makeUser, id);
        if (users.size() != 1) {
            // TODO not found
        }
        return users.get(0);
    }

    private void checkUser(User user) {
        if (user.getLogin().isEmpty() || (user.getLogin().contains(" "))) {
            log.warn("Ошибка написания логина!");
            throw new ValidationException("Ошибка написания логина!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата дня рождения не наступила!");
            throw new ValidationException("Дата дня рождения не наступила!");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    public void checkUserId(long id) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, this::makeUser, id);
        if (users.size() != 1) {
            log.warn("Пользователя с ID " + id + " не найдено!");
            throw new ObjectNotFoundException("Пользователя с ID " + id + " не найдено!");
        }
    }
}