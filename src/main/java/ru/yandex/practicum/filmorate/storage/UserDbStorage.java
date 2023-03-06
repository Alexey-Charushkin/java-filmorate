package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
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


//        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName("USES")
//                .usingGeneratedKeyColumns("USER_ID");
//
//        Number id = simpleJdbcInsert.executeAndReturnKey();
//        System.out.println("Generated id - " + id.longValue());


//        String INSERT_MESSAGE_SQL
//                = "INSERT INTO USERS VALUES (user_id, EMAIL, LOGIN, USER_NAME, BIRTHDATE)", 0, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday().toString();
//        final String SQL = "INSERT INTO ... RETUNING id";
//
//            KeyHolder keyHolder = new GeneratedKeyHolder();
//
//            jdbcTemplate.update(connection -> {
//                PreparedStatement ps = connection
//                        .prepareStatement(INSERT_MESSAGE_SQL, Statement.RETURN_GENERATED_KEYS);
//                               return ps;
//            }, keyHolder);
//
//             keyHolder.getKey();
//        }
  //  }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        user.setId((long) id);
//.usingGeneratedKeyColumns("ID")

    jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?, ?)", id , user.getEmail(), user.getLogin(), user.getName(), user.getBirthday().toString());
      id = Statement.RETURN_GENERATED_KEYS;
//        Integer idOpt = generatedKeyHolder.getKey().intValue();
//        id = idOpt;
    //        return jdbcTemplate.update("INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?)", id, "Bill", "Gates", "USA");

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
