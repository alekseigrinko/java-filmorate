package ru.yandex.practicum.filmorate.storage.genre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final static Logger log = LoggerFactory.getLogger(GenreDbStorage.class);


    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Genre> findAll() {
        final String sqlQuery = "SELECT * FROM GENRES";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, this::makeGenre);
        return genres;
    }

    @Override
    public Genre create(String name) {
        String sqlQuery = "INSERT INTO GENRES (NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Genre genre = new Genre(keyHolder.getKey().longValue(), name);
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"GENRE_ID"});
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);
        return genre;
    }

    @Override
    public void deleteGenre(long genreId) {
        String sqlQuery = "DELETE FROM GENRES WHERE GENRE_ID = ?";
        jdbcTemplate.update(sqlQuery, genreId);
    }

    @Override
    public Genre getGenreById(long genreId) {
        checkGenreId(genreId);
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, this::makeGenre, genreId);
        if (genres.size() != 1) {
            // TODO not found
        }
        return genres.get(0);
    }

    public Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        long genreId = rs.getLong("GENRE_ID");
        String name = rs.getString("NAME");
        return new Genre(genreId, name);
    }

    private void checkGenreId(long id) {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, this::makeGenre, id);
        if (genres.size() != 1) {
            log.warn("Жанра с ID " + id + " не найдено!");
            throw new ObjectNotFoundException("Жанра с ID " + id + " не найдено!");
        }
    }

    @Override
    public void addGenreByFilm(long filmId, long genreId) {
        String sqlQuery = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery,filmId,genreId);
    }

    @Override
    public void deleteGenreByFilmByFilmId(long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    public List<Long> getGenreByFilmId(long filmId) {
        final String sqlQuery = "SELECT GENRE_ID FROM FILM_GENRES " +
                "WHERE FILM_ID = ?";
        List<Long> genres = jdbcTemplate.queryForList(sqlQuery, Long.class, filmId);
        return genres;
    }
}
