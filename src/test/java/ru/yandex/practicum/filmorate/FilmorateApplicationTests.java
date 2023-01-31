package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationDescriptionSizeException;
import ru.yandex.practicum.filmorate.exceptions.ValidationDurationException;
import ru.yandex.practicum.filmorate.exceptions.ValidationReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    FilmController controller = new FilmController();

    Film film = new Film("Super Film", "Super film description",
            LocalDate.of(1967, 3, 25), Duration.ofMinutes(100));

    Film filmFailDescription = new Film("Film name", "Пятеро друзей ( комик-группа «Шарло»)," +
            " приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова," +
            " который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия»," +
            " стал кандидатом Коломбани.",
            LocalDate.of(1900, 3, 25), Duration.ofMinutes(250));

    Film filmFailReleaseDate = new Film("Name", "Description",
            LocalDate.of(1890, 3, 25), Duration.ofMinutes(200));

    Film filmFailDuration = new Film("Film Name", "Film Description",
            LocalDate.of(1890, 3, 25), Duration.ofMinutes(-50));

    @Test
    void contextLoads() { }

    @Test
    void filmValidate() {
        controller.filmValidate(film);
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is("Super Film"));
        assertThat(response.getBody().getDescription(), is("Super film description"));
        assertThat(response.getBody().getReleaseDate().toString(), is("1967-03-25"));
        assertThat(response.getBody().getDuration().toMinutes(), is(100L));
    }

    @Test
    void FilmFailDescriptionValidate() throws RuntimeException {
        assertThrows(ValidationDescriptionSizeException.class, () -> controller.filmValidate(filmFailDescription),
                "Исключение не сгенерировано.");
    }

    @Test
    void FilmFailFailReleaseDateValidate() throws RuntimeException {
        assertThrows(ValidationReleaseDateException.class, () -> controller.filmValidate(filmFailReleaseDate),
                "Исключение не сгенерировано.");
    }

    @Test
    void FilmFailFailDurationValidate() throws RuntimeException {
        assertThrows(ValidationDurationException.class, () -> controller.filmValidate(filmFailDuration),
                "Исключение не сгенерировано.");
    }

}
