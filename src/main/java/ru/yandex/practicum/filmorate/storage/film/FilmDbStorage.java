package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final static Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        final String sqlQuery = "SELECT * FROM FILMS";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm);
        return films;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        System.out.println(film.getMpa().toString());
        long mpaId = film.getMpa().getId();
        System.out.println(mpaId);
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, (int) mpaId);
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        film.setMpa(getMpaRating(mpaId));
        return film;
    }

    /*private void createMpaRating(String nameRating) {
        String sqlQuery = "INSERT INTO MPA_RATINGS (NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"MPA_RATING_ID"});
            stmt.setString(1, nameRating);
            return stmt;
        }, keyHolder);
    }*/

    Mpa getMpaRating(long mpaRatingId) {
        String sqlQuery = "SELECT * FROM MPA_RATINGS WHERE MPA_RATING_ID = ?";
        final List<Mpa> mpaRatings = jdbcTemplate.query(sqlQuery, this::makeMpaRating, mpaRatingId);
        if (mpaRatings.size() != 1) {
            // TODO not found
        }
        return mpaRatings.get(0);
    }

    public Mpa makeMpaRating(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("MPA_RATING_ID"));
        mpa.setName(rs.getString("NAME"));
        return mpa;
    }

    public Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("FILM_ID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        /*int mpaRatingId = rs.getInt("MPA_RATING_ID");*/
        return new Film(id, name, description, releaseDate, duration, getMpaRating(rs.getLong("MPA_RATING_ID")));
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMS SET " +
                "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_RATING = ? " +
                "WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQuery
                    , film.getId()
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getMpa().getId()
            );
        return film;
    }

}
