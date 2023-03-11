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
import ru.yandex.practicum.filmorate.model.Film;
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
    public Film getFilm(Long id) {
        return null;
    }

    @Override
    public List<Film> getFilms() {

            jdbcTemplate = new JdbcTemplate(dataSource);
            return jdbcTemplate.query("SELECT * FROM films", new FilmDbStorage.FilmMapper());
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
                preparedStatement.setString(2, mpa.getMpaRatingName());
            }

            @Override
            public int getBatchSize() {
                return mpaToAdd.size();
            }
        });

        System.out.println(Arrays.toString(rowsAffected) + " rows affected");
    }
    private boolean isTableEmpty(JdbcTemplate jdbcTemplate, String tableName) {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
        return count == 0;
    }

    @Override
    public void add(Film film) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate = new JdbcTemplate(dataSource);

        if (isTableEmpty(jdbcTemplate, "mpa_rating")) addFilmsRatings();

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

        log.info("rowsAffected = {}, id={}", rowsAffected, id);
        log.info("Фильм добавлен {}.", film);
    }

    @Override
    public void addMPARating(Film film) {
        String sql = "INSERT INTO films (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, film.getId(), film.getMpa().getId());
    }

    @Override
    public void update(Film film) {

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
}
