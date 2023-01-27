package model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@EqualsAndHashCode

public class User {

    private Integer userId;

    private String email;

    private String login;

    private String userName;

    private LocalDate birthday;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public User(String userId, String email, String login, String userName, String birthday) {
        this.userId = Integer.parseInt(userId);
        this.email = email;
        this.login = login;
        this.userName = userName;
        this.birthday = LocalDate.parse(birthday, format);
    }


}
