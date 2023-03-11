package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Objects;

@Component("UserDbStorage")
@Primary
@RequiredArgsConstructor
@Log4j2
public class UserDbStorage implements UserDaoStorage {
    private DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User findUserById(Long idUser) {
        User user;
        try {
            jdbcTemplate = new JdbcTemplate(dataSource);
            user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?"
                    , new UserMapper(), idUser);
            log.info("Пользователь с id {} найден.", idUser);
        } catch (Exception ex) {
            log.info("Ошибка! Пользователь с id {} не найден.", idUser);
            throw new UserNotFoundException("Ошибка! Пользователь с id " + idUser + " не найден.");
        }
        List<Long> userFriendsId = findUserFriendsById(idUser);
        for (Long id : userFriendsId) {
            user.setUserFriendsId(id);
        }
        //defineFriendStatus(Long userId, Long friendId)
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

        Long id = (long) Objects.requireNonNull(generatedKeyHolder.getKey()).intValue();
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

        jdbcTemplate.update("DELETE FROM users where user_id = ?", id);
        log.info("Пользователь с id {} удалён.", id);
    }

    public void addFriend(Long userId, Long friendId) {

        if (isFriendExist(userId, friendId)) {
            log.info("Пользователь с id {} уже является другом пользователя с id {}", friendId, userId);
            return;
        }
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = "INSERT INTO `users_friends_id`(`user_id`, `friend_id`, `friend_status`) VALUES(?, ?, ?);";

        int rowsAffected =
                jdbcTemplate.update(conn -> {

                    PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setLong(1, userId);
                    preparedStatement.setLong(2, friendId);
                    preparedStatement.setString(3, "неподтверждённая");

                    return preparedStatement;

                }, generatedKeyHolder);

        Long id = (long) Objects.requireNonNull(generatedKeyHolder.getKey()).intValue();

        defineFriendStatus(userId, friendId);

        log.info("rowsAffected = {}, id={}", rowsAffected, id);
        log.info("Пользователь с id {} добавлен в друзья к пользователю с id {}", friendId, userId);

    }

    public void defineFriendStatus(Long userId, Long friendId) {

        String friendStatus = "";

        if (isFriendExist(userId, friendId) && isFriendExist(friendId, userId)) {
            friendStatus = "подтверждённая";
            jdbcTemplate.update("UPDATE users_friends_id SET friend_status = ? WHERE user_id = ?", friendStatus, friendId);
            jdbcTemplate.update("UPDATE users_friends_id SET friend_status = ? WHERE user_id = ?", friendStatus, userId);
        } else {
            friendStatus = "неподтверждённая";
            jdbcTemplate.update("UPDATE users_friends_id SET friend_status = ? WHERE user_id = ?", friendStatus, friendId);
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        if (!isFriendExist(userId, friendId)) {
            log.info("Дружба пользователей  с id {} и id {} не обранужена.", friendId, userId);
            return;
        }
        jdbcTemplate.update("DELETE FROM users_friends_id where user_id = ? AND friend_id = ?",
                userId, friendId);

        defineFriendStatus(userId, friendId);
        log.info("Пользователь  с id {} удалён из друзей пользователя с id {}.", friendId, userId);
    }

    @Override
    public List<Long> findUserFriendsById(Long idUser) {
        List<Long> friendsId;
        jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            friendsId = jdbcTemplate.queryForList("SELECT friend_id FROM users_friends_id WHERE user_id = ? ",
                    Long.class, idUser);
            log.info("Количество друзей пользователя с id {}: {}.", idUser, friendsId.size());
        } catch (Exception ex) {
            log.info("У пользователя с id {} нет друзей.", idUser);
            throw new UserNotFoundException("Список друзей пользователя с id " + idUser + " пуст.");
        }
        return friendsId;
    }

    @Override
    public boolean isFriendExist(Long userId, Long friendId) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT COUNT(*) FROM users_friends_id WHERE user_id = ? AND friend_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{userId, friendId}, Integer.class);
        return count > 0;
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
