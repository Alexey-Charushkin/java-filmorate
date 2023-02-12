package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.Email;
import java.time.LocalDate;

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

}
