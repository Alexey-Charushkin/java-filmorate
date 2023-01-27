package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@EqualsAndHashCode
//@AllArgsConstructor

public class User {

    private Integer id = 0;

    private String email;

    private String login;

    private String name;

    private LocalDate birthday;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public User(String email, String login, String name, String birthday) {
        this.id = id++;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = LocalDate.parse(birthday, formatter);
    }
}
