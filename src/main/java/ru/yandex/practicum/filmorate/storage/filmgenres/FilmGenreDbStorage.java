package ru.yandex.practicum.filmorate.storage.filmgenres;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FilmGenreDbStorage implements FilmGenreStorage{
   private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<FilmGenre> findAll() {
        final String sqlQuery = "SELECT * FROM FILM_GENRES";
        final List<FilmGenre> filmGenres = jdbcTemplate.query(sqlQuery, this::makeFilmGenre);
        return filmGenres;
    }

    @Override
    public void create(long filmId, long genreId) {
        String sqlQuery = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_GENRE_ID"});
            stmt.setLong(1, filmId);
            stmt.setLong(2, genreId);
            return stmt;
        }, keyHolder);
    }

    @Override
    public void deleteFilmGenreByFilmId(long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    public List<Long> getGenreByFilmId(long filmId) {
        final String sqlQuery = "SELECT GENRE_ID FROM FILM_GENRES " +
                "WHERE FILM_ID = ?";
        List<Long> genres = jdbcTemplate.queryForList(sqlQuery, Long.class, filmId);
        return genres;
    }

    public FilmGenre makeFilmGenre(ResultSet rs, int rowNum) throws SQLException {
        long filmGenreId = rs.getLong("FILM_GENRE_ID");
        long filmId = rs.getLong("FILM_ID");
        long genreId = rs.getLong("GENRE_ID");
        return new FilmGenre(filmGenreId, filmId, genreId);
    }
}
