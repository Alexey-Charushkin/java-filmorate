package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Validate;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FilmController filmController;
    @Autowired
    private UserController userController;
    private final UserDbStorage userStorage;
    @Autowired
    private Validate validate;

    User userFailName = new User("friend@common.ru",
            "userLogin", null, LocalDate.of(2000, 8, 20), null, null);
    Film film = new Film("Super Film", "Super film description",
            LocalDate.of(1967, 3, 25), 100, null, null, null, null);
    Film filmFailDescription = new Film("Film name", "Пятеро друзей ( комик-группа «Шарло»)," +
            " приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова," +
            " который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия»," +
            " стал кандидатом Коломбани.",
            LocalDate.of(1900, 3, 25), 250, null, null, null, null);
    Film filmFailReleaseDate = new Film("Name", "Description",
            LocalDate.of(1890, 3, 25), 200, null, null, null, null);
    Film filmFailDuration = new Film("Film Name", "Film Description",
            LocalDate.of(1890, 3, 25), -50, null, null, null, null);

    @Test
    void contextLoads() {
    }

    @Test
    void filmValidate() {
        validate.validate(film);
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).getId(), notNullValue());
        assertThat(response.getBody().getName(), is("Super Film"));
        assertThat(response.getBody().getDescription(), is("Super film description"));
        assertThat(response.getBody().getReleaseDate().toString(), is("1967-03-25"));
        assertThat(response.getBody().getDuration(), is(100));
    }

    @Test
    void FilmFailFailReleaseDateValidate() throws RuntimeException {
        assertThrows(ValidationException.class, () -> validate.validate(filmFailReleaseDate),
                "Исключение не сгенерировано.");
    }

    // новые тесты -----------------------------------------------------------------------------------
//    @Test
//    public void testFindUserById() {

//        Optional<User> userOptional = userStorage.findUserById(1);
//
//        assertThat(userOptional)
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
//                );
//    }
}
