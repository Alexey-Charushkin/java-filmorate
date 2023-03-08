package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Component("UserDbStorage")
@Primary
@RequiredArgsConstructor
@Log4j2
public class UserDbStorage implements UserStorage {
    private Long id;
    private DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User findUserById(Long idUser) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        User user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?", new Object[]{idUser}, new UserMapper());
        log.info("Пользователь с id {} найден.", idUser);
        return user;
    }

    @Override
    public List<User> getUsers() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query("SELECT * FROM users", new UserMapper());
    }

    @Override
    public void add(User user) {

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = "INSERT INTO `users`(`email`, `login`, `user_name`, `birthdate`) VALUES(?, ?, ?, ?);";

        int rowsAffected =
                jdbcTemplate.update(conn -> {

                    PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, user.getEmail());
                    preparedStatement.setString(2, user.getLogin());
                    preparedStatement.setString(3, user.getName());
                    preparedStatement.setObject(4, user.getBirthday());

                    return preparedStatement;

                }, generatedKeyHolder);

        id = (long) generatedKeyHolder.getKey().intValue();
        user.setId(id);

        log.info("rowsAffected = {}, id={}", rowsAffected, id);
        log.info("Пользователь добавлен {}.", user);
    }

    @Override
    public void update(User user) {

        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(
                "UPDATE users SET email = ?, login = ?, user_name = ?, birthdate = ? where user_id = ?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());

        log.info("Пользователь обновлён с id {}, {}.", user.getId(), user);
    }

    @Override
    public void remove(Long id) {
        this.jdbcTemplate.update("DELETE FROM users where user_id = ?",
                Long.valueOf(id));
        log.info("Пользователь с id {} удалён.", id);
    }

    private static final class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("user_name"));
            user.setBirthday(rs.getDate("birthdate").toLocalDate());
            return user;
        }
    }

}
