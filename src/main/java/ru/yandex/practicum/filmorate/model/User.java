package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User extends Item {
    @Email
    private String email;
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
    private List<User> userFriends;

    public void removeFriends(User user) {
        userFriends.remove(user);
    }
}
