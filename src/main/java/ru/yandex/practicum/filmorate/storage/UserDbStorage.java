package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Component("UserDbStorage")
@Primary
@RequiredArgsConstructor
@Log4j2
public class UserDbStorage implements UserStorage {

    private Long id;
    private DataSource dataSource;
    @Autowired
    public UserDbStorage(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User findUserById(Long id) {
        return users.get(id);
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public void add(User user) {

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = "INSERT INTO `USERS`(`email`, `login`, `user_name`, `birthdate`) VALUES(?, ?, ?, ?);";

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
        //  users.put(user.getId(), user);
    }

    @Override
    public void remove(User user) {
        users.remove(user);
    }
}
