package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Component("FilmDbStorage")
@Primary
@RequiredArgsConstructor
@Log4j2
public class FilmDbStorage implements FilmDaoStorage {
    private DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Film getFilm(Long idFilm) {
        Film film;
        try {
            jdbcTemplate = new JdbcTemplate(dataSource);
            film = jdbcTemplate.queryForObject("SELECT * FROM films WHERE film_id = ?"
                    , new FilmDbStorage.FilmMapper(), idFilm);
            log.info("Фильм с id {} найден.", idFilm);
        } catch (Exception ex) {
            log.info("Ошибка! Фильм с id {} не найден.", idFilm);
            throw new FilmNotFoundException("Ошибка! Фильм с id " + idFilm + " не найден.");
        }
        film.getMpa().setName(getMPAName(film.getMpa().getId()));
        film.setGenres(getGenresByIdFilm(film.getId()));

        return film;
    }

    @Override
    public List<Film> getFilms() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Film> films = jdbcTemplate.query("SELECT * FROM films", new FilmDbStorage.FilmMapper());
        for (Film film : films) {
            film.getMpa().setName(getMPAName(film.getMpa().getId()));
            film.setGenres(getGenresByIdFilm(film.getId()));
        }
        return films;
    }

    @Override
    public void add(Film film) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate = new JdbcTemplate(dataSource);

        if (isTableEmpty(jdbcTemplate, "mpa_rating")) addFilmsRatings();
        if (isTableEmpty(jdbcTemplate, "genre")) addGenres();

        String sql = "INSERT INTO `films`(`film_name`, `description`, `release_date`, `duration`, `mpa_rating_id`, `rate`)" +
                " VALUES(?, ?, ?, ?, ?, ?);";

        int rowsAffected =
                jdbcTemplate.update(conn -> {

                    PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, film.getName());
                    preparedStatement.setString(2, film.getDescription());
                    preparedStatement.setObject(3, film.getReleaseDate());
                    preparedStatement.setInt(4, film.getDuration());
                    preparedStatement.setObject(5, film.getMpa().getId());
                    preparedStatement.setInt(6, film.getRate());

                    return preparedStatement;

                }, generatedKeyHolder);

        Long id = (long) Objects.requireNonNull(generatedKeyHolder.getKey()).intValue();

        film.setId(id);
        film.getMpa().setName(getMPAName(film.getMpa().getId()));

        removeGenres(id);
        addFilmsGenresId(film);
        film.setGenres((getGenresByIdFilm(film.getId())));

        log.info("rowsAffected = {}, id={}", rowsAffected, id);
        log.info("Фильм добавлен {}.", film);
    }

    @Override
    public void update(Film film) {
        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(
                "UPDATE films SET film_name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ?," +
                        "rate = ? where film_id = ?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(),
                film.getRate(), film.getId());

        film.getMpa().setName(getMPAName(film.getMpa().getId()));

        removeGenres(film.getId());

        addFilmsGenresId(film);

        film.setGenres((getGenresByIdFilm(film.getId())));

        log.info("Фильм обновлён с id {}, {}.", film.getId(), film);
    }

    @Override
    public void addLike(Long filmId, Long userId) {

        if (isLikeExist(filmId, userId)) {
            log.info("Пользователь с id {} уже поставил лайк фильму с id {}", userId, filmId);
            return;
        }
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = "INSERT INTO `users_films_like`(`user_id`, `film_id`) VALUES(?, ?);";

        int rowsAffected =
                jdbcTemplate.update(conn -> {

                    PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setLong(1, userId);
                    preparedStatement.setLong(2, filmId);

                    return preparedStatement;

                }, generatedKeyHolder);

        Long id = (long) Objects.requireNonNull(generatedKeyHolder.getKey()).intValue();

        Integer rate = getFilm(filmId).getRate();
        changeRate(filmId, ++rate);

        log.info("rowsAffected = {}, id={}", rowsAffected, id);
        log.info("Пользователь с id {} добавил лайк фильму с id {}", userId, filmId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        if (!isLikeExist(filmId, userId)) {
            log.info("Пользователь с  id {} не ставил лайк фильму с id {}.", userId, filmId);
            return;
        }
        jdbcTemplate.update("DELETE FROM users_films_like where user_id = ? AND film_id = ?",
                userId, filmId);

        Integer rate = getFilm(filmId).getRate();
        changeRate(filmId, --rate);

        log.info("Пользователь  с id {} удалил лайк фильму с id {}.", userId, filmId);
    }

    @Override
    public void addMPARating(Film film) {
        String sql = "INSERT INTO films (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, film.getId(), film.getMpa().getId());
    }

    @Override
    public List<Genre> getAllGenres() {
        jdbcTemplate = new JdbcTemplate(dataSource);

        List<Genre> genres = jdbcTemplate.query("SELECT * FROM genre",
                new FilmDbStorage.GenreMapper());
        return genres;
    }

    @Override
    public Genre getGenreById(Long id) {

        Genre genre;
        jdbcTemplate = new JdbcTemplate(dataSource);

        try {
            genre = jdbcTemplate.queryForObject("SELECT * FROM genre WHERE genre_id = ?",
                    new GenreMapper(), id);
        } catch (Exception ex) {
            log.info("Ошибка! Жанр фильма с id {} не найден.", id);
            throw new FilmNotFoundException("Ошибка! Жанр фильма с id " + id + " не найден.");
        }
        return genre;
    }

    @Override
    public List<MPA> getAllMPA() {
        jdbcTemplate = new JdbcTemplate(dataSource);

        List<MPA> mpas = jdbcTemplate.query("SELECT * FROM mpa_rating",
                new FilmDbStorage.MPAMapper());
        return mpas;
    }

    @Override
    public MPA getMPAById(Long id) {
        MPA mpa;
        jdbcTemplate = new JdbcTemplate(dataSource);

        try {
            mpa = jdbcTemplate.queryForObject("SELECT * FROM mpa_rating WHERE id = ?",
                    new MPAMapper(), id);
        } catch (Exception ex) {
            log.info("Ошибка! MPA рейтинг фильма с id {} не найден.", id);
            throw new FilmNotFoundException("Ошибка! MPA рейтинг фильма с id " + id + " не найден.");
        }
        return mpa;
    }

    public List<Genre> getGenresByIdFilm(Long idFilm) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Genre> genres = jdbcTemplate.query("SELECT fg.genre_id, g.genre_name FROM films_genres AS fg JOIN " +
                        "genre AS g ON fg.genre_id = g.genre_id WHERE fg.film_id = ?",
                new Object[]{idFilm}, new FilmDbStorage.GenreMapper());
        for (Genre genre : genres) {
            genre.setName(getGenreName(genre.getId()));
        }
        return genres;
    }

    private void removeGenres(Long idFilm) {
        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update("DELETE FROM films_genres where film_id = ?", idFilm);

    }

    private String getMPAName(Integer idMPA) {
        String mpaName;
        try {
            jdbcTemplate = new JdbcTemplate(dataSource);
            mpaName = jdbcTemplate.queryForObject("SELECT mpa_rating_name FROM mpa_rating WHERE id = ?",
                    new Object[]{idMPA}, String.class);

            log.info("MPA рейтинг с id {} найден.", idMPA);
            return mpaName;
        } catch (Exception ex) {
            log.info("Ошибка! MPA рейтинг с id {} не найден.", idMPA);
            throw new FilmNotFoundException("Ошибка! MPA рейтинг с id " + idMPA + " не найден.");
        }
    }

    private String getGenreName(Integer idGenre) {
        String genreName;
        try {
            jdbcTemplate = new JdbcTemplate(dataSource);
            genreName = jdbcTemplate.queryForObject("SELECT genre_name FROM genre WHERE  genre_id = ?",
                    new Object[]{idGenre}, String.class);

            log.info("Жанр с id {} найден.", idGenre);
            return genreName;
        } catch (Exception ex) {
            log.info("Ошибка! Жанр с id {} не найден.", idGenre);
            throw new FilmNotFoundException("Ошибка! Жанр с id " + idGenre + " не найден.");
        }
    }

    private void changeRate(Long filmId, Integer rate) {

        jdbcTemplate.update("UPDATE films SET rate = ? WHERE film_id = ?", rate, filmId);

    }

    private boolean isLikeExist(Long filmId, Long userId) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT COUNT(*) FROM users_films_like WHERE user_id = ? AND film_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{userId, filmId}, Integer.class);
        return count > 0;
    }

    private boolean isTableEmpty(JdbcTemplate jdbcTemplate, String tableName) {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
        return count == 0;
    }

    private static final class GenreMapper implements RowMapper<Genre> {
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre();

            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return genre;
        }
    }

    private static final class MPAMapper implements RowMapper<MPA> {
        public MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
            MPA mpa = new MPA();

            mpa.setId(rs.getInt("id"));
            mpa.setName(rs.getString("mpa_rating_name"));
            return mpa;
        }
    }

    private static final class FilmMapper implements RowMapper<Film> {
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film();

            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("film_name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            MPA mpa = new MPA();
            mpa.setId(rs.getInt("mpa_rating_id"));
            film.setMpa(mpa);
            film.setRate(rs.getInt("rate"));
            return film;
        }
    }

    private void addFilmsRatings() {
        jdbcTemplate = new JdbcTemplate(dataSource);

        List<MPA> mpaToAdd = new ArrayList<>();
        mpaToAdd.add(new MPA(1, "G"));
        mpaToAdd.add(new MPA(2, "PG"));
        mpaToAdd.add(new MPA(3, "PG-13"));
        mpaToAdd.add(new MPA(4, "R"));
        mpaToAdd.add(new MPA(5, "NC-17"));

        String sql = "INSERT INTO mpa_rating (id, mpa_rating_name) VALUES (?, ?)";
        int[] rowsAffected = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                MPA mpa = mpaToAdd.get(i);
                preparedStatement.setInt(1, mpa.getId());
                preparedStatement.setString(2, mpa.getName());
            }

            @Override
            public int getBatchSize() {
                return mpaToAdd.size();
            }
        });
        log.info("rowsAffected = {}", rowsAffected);
    }

    private void addFilmsGenresId(Film film) {

        jdbcTemplate = new JdbcTemplate(dataSource);

        if (film.getGenres() == null) return;

        List<Genre> genres = new ArrayList<>();
        genres.addAll(film.getGenres());

        List<Genre> genresToAdd = new ArrayList<>();
        genresToAdd.addAll(removeGenreDuplicates(genres));

        String sql = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
        int[] rowsAffected = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Genre genre = genresToAdd.get(i);
                preparedStatement.setLong(1, film.getId());
                preparedStatement.setInt(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return genresToAdd.size();
            }
        });
        log.info("rowsAffected = {}", rowsAffected);
    }

    private void addGenres() {
        jdbcTemplate = new JdbcTemplate(dataSource);

        List<String> genresNameToAdd = new ArrayList<>();
        genresNameToAdd.add("Комедия");
        genresNameToAdd.add("Драма");
        genresNameToAdd.add("Мультфильм");
        genresNameToAdd.add("Триллер");
        genresNameToAdd.add("Документальный");
        genresNameToAdd.add("Боевик");

        String sql = "INSERT INTO genre (genre_id, genre_name) VALUES (?, ?)";
        int[] rowsAffected = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                String name = genresNameToAdd.get(i);
                preparedStatement.setInt(1, i + 1);
                preparedStatement.setString(2, name);
            }

            @Override
            public int getBatchSize() {
                return genresNameToAdd.size();
            }
        });
        System.out.println(Arrays.toString(rowsAffected) + " rows affected");
    }

    private List<Genre> removeGenreDuplicates(List<Genre> genres) {

        List<Genre> genresToSet = new ArrayList<>(genres);

        Set<Integer> genresId = new HashSet<>();

        for (Genre genre : genres) {
            genresId.add(genre.getId());
        }
        genresToSet.clear();
        for (Integer id : genresId) {
            Genre genre = new Genre();
            genre.setId(id);
            genresToSet.add(genre);
        }
        return genresToSet;
    }
}
