package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@FieldDefaults(level=AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User extends Item {
    @Email
    String email;
    String login;
    String name;
    @NonNull
    LocalDate birthday;
    @JsonIgnore
    Set<Long> userFriendsId = new HashSet<>();

    public void setUserFriendsId(Long friendsId) {
        userFriendsId.add(friendsId);
    }

    public Set<Long> getUserFriendsId() {
        return userFriendsId;
    }
}
