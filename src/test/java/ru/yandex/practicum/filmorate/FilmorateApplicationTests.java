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
import ru.yandex.practicum.filmorate.model.Film;


import java.time.Duration;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;


import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateApplicationTests {
	@Autowired
	private TestRestTemplate restTemplate;

	FilmController controller = new FilmController();
	Film film = new Film("nisi eiusmod", "adipisicing",
			LocalDate.of(1967, 03, 25), Duration.ofMinutes(100));

	@Test
	void contextLoads() {

	}
    @Test
	void filmValidate (){
		controller.filmValidate(film);
		ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getId(), notNullValue());
		assertThat(response.getBody().getName(), is("nisi eiusmod"));

	}


}
