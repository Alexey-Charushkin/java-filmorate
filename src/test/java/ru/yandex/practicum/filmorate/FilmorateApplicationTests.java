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
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Validate;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

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
    private final UserDaoStorage userStorage;
    @Autowired
    private Validate validate;

    User user1 = new User("user1@common.ru",
            "user1", "Vasja", LocalDate.of(2000, 8, 24), null);

    User user2 = new User("user2@common.ru",
            "user2", "Koljan", LocalDate.of(1996, 5, 16), null);

    User user3 = new User("updateuser2@common.ru",
            "updateuser2", "Koljan", LocalDate.of(1996, 5, 25), null);

    User userFailName = new User("friend@common.ru",
            "userLogin", null, LocalDate.of(2000, 8, 20), null);
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

    @Test
    public void testAddUser() {
        long userId = 1L;
        userStorage.add(user1);
        Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(userId));

        assertTrue(userOptional.isPresent());
        assertThat(userOptional.get().getId(), is(userId));

    }

    @Test
    public void testRemoveUserById() throws RuntimeException {
        long userId = 1L;
        userStorage.add(user1);
        userStorage.add(user2);

        Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(userId));

        assertTrue(userOptional.isPresent());
        assertThat(userOptional.get().getId(), is(userId));

        userStorage.remove(userId);

        assertThrows(UserNotFoundException.class, () -> userStorage.findUserById(userId),
                "Исключение не сгенерировано.");
    }

    @Test
    public void testFindUserById() {

        long userId = 2L;
        userStorage.add(user1);
        userStorage.add(user2);
        Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(userId));

        assertTrue(userOptional.isPresent());
        assertThat(userOptional.get().getId(), is(userId));

    }

    @Test
    public void testGetUsers() {

        userStorage.add(user1);
        userStorage.add(user2);
        List<User> userList = userStorage.getUsers();

        assertNotNull(userList);
        assertThat(userList.size(), equalTo(2));

    }

    @Test
    public void updateUser() {
        userStorage.add(user1);
        userStorage.add(user2);
        long userId = 2L;
        User updateUser = new User("updateuser2@common.ru",
                "updateuser2", "Koljan", LocalDate.of(1996, 5, 25), null);

        updateUser.setId(userId);
        userStorage.update(updateUser);

        Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(userId));

        assertTrue(userOptional.isPresent());
        assertThat(userOptional.get().getId(), is(userId));
        assertThat(userOptional.get().getEmail(), is("updateuser2@common.ru"));
        assertThat(userOptional.get().getLogin(), is("updateuser2"));
    }

    @Test
    public void testAddFriend() {

        userStorage.add(user1);
        userStorage.add(user2);

        userStorage.addFriend(1L, 2L);

        assertTrue(userStorage.isFriendExist(1L, 2L));
    }

    @Test
    public void testRemoveFriend() {

        userStorage.add(user1);
        userStorage.add(user2);

        userStorage.addFriend(1L, 2L);

        assertTrue(userStorage.isFriendExist(1L, 2L));


        userStorage.removeFriend(1L, 2L);

        assertFalse(userStorage.isFriendExist(1L, 2L));
    }

    @Test
    public void testFindUserFriendsIdByUserId() {

        userStorage.add(user1);
        userStorage.add(user2);
        userStorage.add(user3);

        userStorage.addFriend(1L, 2L);
        userStorage.addFriend(1L, 3L);

        List<Long> idFriends = userStorage.findUserFriendsById(1L);
        assertThat(idFriends.size(), equalTo(2));

    }


}
