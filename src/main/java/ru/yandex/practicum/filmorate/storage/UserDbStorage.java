package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Component("UserDbStorage")
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    Integer id = 0;
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
        String email = user.getEmail();
        String login = user.getLogin();
        String nickName = user.getName();
        String localDate = user.getBirthday().toString();

//        String sql = "INSERT INTO users (email, login, user_name, birthdate) VALUES (user.getEmail(), user.getLogin()," +
//                " user.getName();, user.getBirthday().toString())";
//        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
//                .withTableName("USES")
//                .usingGeneratedKeyColumns("USER_ID");
//
//        Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
//        System.out.println("Generated id - " + id.longValue());

       GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        ResultSet rs;

        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?, ?)", user, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday().toString()).usingGeneratedKeyColumns("ID");

//        Integer idOpt = generatedKeyHolder.getKey().intValue();
//        id = idOpt;
    //        return jdbcTemplate.update("INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?)", id, "Bill", "Gates", "USA");

//        String sql = "INSERT INTO users (email, login, user_name, birthdate) VALUES ('mail@mail.ru', 'dolore', 'Nick Name', '1946-08-20')";
//        users.put(user.getId(), user);
//
//        Statement statement = conn.createStatement();
//        int rows = statement.executeUpdate("INSERT Products(ProductName, Price) VALUES ('iPhone X', 76000)," +
//                "('Galaxy S9', 45000), ('Nokia 9', 36000)");
//        System.out.printf("Added %d rows", rows);
    }


//    @Override
//    public Collection<Post> findPostByUser(User user) {
//
//        String sql = "select * from cat_post where author_id = ? order by creation_date desc";
//
//        return jdbcTemplate.query(sql, (rs, rowNum) -> makePost(user, rs), user.getId());
//    }

    //    private Post makePost(User user, ResultSet rs) throws SQLException {
//
//        Integer id = rs.getInt("id");
//        String description = rs.getString("description");
//        String photoUrl = rs.getString("photo_url");
//        LocalDate creationDate = rs.getDate("creation_date").toLocalDate();
//        return new Post(id, user, description, photoUrl, creationDate);
//    }

    @Override
    public void update(User user) {
      //  users.put(user.getId(), user);
    }

    @Override
    public void remove(User user) {
        users.remove(user);
    }
}
