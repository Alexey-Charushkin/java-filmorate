package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User extends Item {
    @Email
    private String email;
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
    @JsonIgnore
    private Set<Long> userFriendsId = new HashSet<>();

    public void setUserFriendsId(Long friendsId) {
        userFriendsId.add(friendsId);
    }

    public Set<Long> getUserFriendsId() {
        return userFriendsId;
    }
}
