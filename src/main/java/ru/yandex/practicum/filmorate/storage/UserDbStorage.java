package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@Component("UserDbStorage")
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
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
        users.put(user.getId(), user);
    }

//    private Post makePost(User user, ResultSet rs) throws SQLException {
//
//        Integer id = rs.getInt("id");
//        String description = rs.getString("description");
//        String photoUrl = rs.getString("photo_url");
//        LocalDate creationDate = rs.getDate("creation_date").toLocalDate();
//        return new Post(id, user, description, photoUrl, creationDate);
//    }
//    @Override
//    public Collection<Post> findPostByUser(User user) {
//
//        String sql = "select * from cat_post where author_id = ? order by creation_date desc";
//
//        return jdbcTemplate.query(sql, (rs, rowNum) -> makePost(user, rs), user.getId());
//    }
    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void remove(User user) {
        users.remove(user);
    }
}
