package ru.yandex.practicum.filmorate.storage.mpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDbStorage implements MpaStorage{

    private  final JdbcTemplate jdbcTemplate;
    private final static Logger log = LoggerFactory.getLogger(FilmDbStorage.class);


    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaRating(long mpaRatingId) {
        checkMpaId(mpaRatingId);
        String sqlQuery = "SELECT * FROM MPA_RATINGS WHERE MPA_RATING_ID = ?";
        final List<Mpa> mpaRatings = jdbcTemplate.query(sqlQuery, this::makeMpaRating, mpaRatingId);
        if (mpaRatings.size() != 1) {
            // TODO not found
        }
        return mpaRatings.get(0);
    }

    @Override
    public List<Mpa> findAllMpa() {
        final String sqlQuery = "SELECT * FROM MPA_RATINGS";
        final List<Mpa> mpaRatings = jdbcTemplate.query(sqlQuery, this::makeMpaRating);
        return mpaRatings;
    }

    public Mpa makeMpaRating(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("MPA_RATING_ID"));
        mpa.setName(rs.getString("NAME"));
        return mpa;
    }

    private void checkMpaId(long id) {
        String sqlQuery = "SELECT * FROM MPA_RATINGS WHERE MPA_RATING_ID = ?";
        final List<Mpa> mpaRatings = jdbcTemplate.query(sqlQuery, this::makeMpaRating, id);
        if (mpaRatings.size() != 1) {
            log.warn("MPA-рейтинга с ID " + id + " не найдено!");
            throw new ObjectNotFoundException("MPA-рейтинга с ID " + id + " не найдено!");
        }
    }
}
