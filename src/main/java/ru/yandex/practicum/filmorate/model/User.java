package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class User extends Item {

    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @NonNull
    @Past
    private LocalDate birthday;

}
