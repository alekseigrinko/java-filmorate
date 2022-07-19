package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.filmlike.FilmLikeDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final GenreDbStorage genreDbStorage;
    private final FilmLikeDbStorage filmLikeDbStorage;
    private final static Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage
    , FilmLikeDbStorage filmLikeDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.filmLikeDbStorage = filmLikeDbStorage;
    }

    @Override
    public List<Film> findAll() {
        final String sqlQuery = "SELECT * FROM FILMS";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm);
        return films;
    }

    @Override
    public Film create(Film film) {
        checkFilm(film);
        String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
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
            stmt.setInt(5, (int) film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        film.setMpa(getMpaRating(film.getMpa().getId()));
        updateFilmGenre(film);
        return film;
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

    public Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("FILM_ID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        return new Film(id, name, description, releaseDate, duration, getMpaRating(rs.getLong("MPA_RATING")));
    }

    @Override
    public Film update(Film film) {
        checkFilmId(film.getId());
        checkFilm(film);
        String sqlQuery = "UPDATE FILMS " +
                "SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_RATING = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId()
        );
        genreDbStorage.deleteGenreByFilmByFilmId(film.getId());
        updateFilmGenre(film);
        return film;
    }

    @Override
    public void deleteFilmById(long filmId) {
        checkFilmId(filmId);
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public Film getFilmById(long id) {
        checkFilmId(id);
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        if (films.size() != 1) {
            // TODO not found
        }
        Film film = films.get(0);
        List<Genre> genresWithName = genreDbStorage.getGenreByFilmId(film.getId()).stream()
                .map(genreDbStorage::getGenreById)
                .collect(Collectors.toList());
        film.setGenres(genresWithName);
        return film;
    }

    void checkFilm(Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            log.warn("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Размер описания фильма превышает 200 символов!");
            throw new ValidationException("Размер описания фильма превышает 200 символов!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Некорректная дата релиза!");
            throw new ValidationException("Некорректная дата релиза!");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма не может быть меньше 0");
            throw new ValidationException("Продолжительность фильма не может быть меньше 0");
        }
    }

    public void checkFilmId(long id) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        if (films.size() != 1) {
            log.warn("Фильма с ID " + id + " не найдено!");
            throw new ObjectNotFoundException("Фильма с ID " + id + " не найдено!");
        }
    }

    private void checkMpaId(long id) {
        String sqlQuery = "SELECT * FROM MPA_RATINGS WHERE MPA_RATING_ID = ?";
        final List<Mpa> mpaRatings = jdbcTemplate.query(sqlQuery, this::makeMpaRating, id);
        if (mpaRatings.size() != 1) {
            log.warn("MPA-рейтинга с ID " + id + " не найдено!");
            throw new ObjectNotFoundException("MPA-рейтинга с ID " + id + " не найдено!");
        }
    }

    private void updateFilmGenre(Film film) {
        if (film.getGenres().size() != 0) {
            film.getGenres().stream().distinct().forEach(genre -> genreDbStorage.addGenreByFilm(film.getId(), genre.getId()));
            List<Genre> genresWithName = film.getGenres().stream()
                    .distinct()
                    .map(genre -> genreDbStorage.getGenreById(genre.getId()))
                    .collect(Collectors.toList());
            film.setGenres(genresWithName);
        }
    }

    @Override
    public List<Film> getPopularFilms(long count) {
        List<Long> likes = filmLikeDbStorage.findAll();
        if (likes.size() != 0) {
            List<Film> films = filmLikeDbStorage.getPopularFilms(count).stream()
                    .map(this::getFilmById)
                    .collect(Collectors.toList());
            return films;
        } else {
            return findAll();
        }
    }

    @Override
    public String putLikeByFilmIdAndUserId(long id, long userId) {
        checkFilmId(id);
        filmLikeDbStorage.create(id, userId);
        return "Поставлен лайк фильму ID: " + id + ", пользователем ID: " + userId;
    }

    @Override
    public String deleteLikeByFilmIdAndUserId(long id, long userId) {
        checkFilmId(id);
        filmLikeDbStorage.deleteFilmLike(id, userId);
        return "Лайк фильму ID: " + id + ", пользователя ID: " + userId + " удален";
    }

}
