package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Validate;
import ru.yandex.practicum.filmorate.storage.FilmDaoStorage;
import ru.yandex.practicum.filmorate.storage.UserDaoStorage;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserDaoStorage userStorage;

    private final FilmDaoStorage filmStorage;
    @Autowired
    private Validate validate;

    User user1;
    User user2;
    User user3;
    Film filmFailReleaseDate;
    MPA mpa;
    Genre genre;
    List<Genre> genres;
    Set<Long> usersFilmsLikes;
    Film film;
    Film film2;

    @BeforeEach
    public void beforeEach() {

        user1 = new User("user1@common.ru",
                "user1", "Vasja", LocalDate.of(2000, 8, 24), null);
        user2 = new User("user2@common.ru",
                "user2", "Koljan", LocalDate.of(1996, 5, 16), null);

        user3 = new User("updateuser2@common.ru",
                "updateuser2", "Koljan", LocalDate.of(1996, 5, 25), null);


        mpa = new MPA();
        mpa.setId(1);
        genre = new Genre();
        genre.setId(2);
        genres = new ArrayList<>();
        genres.add(genre);
        usersFilmsLikes = new HashSet<>();

        film = new Film("Super Film", "Super film description",
                LocalDate.of(1967, 3, 25), 100, 0, mpa, genres, usersFilmsLikes);

        film2 = new Film("Super Film2", "Super film description2",
                LocalDate.of(1967, 11, 14), 100, 0, mpa, genres, usersFilmsLikes);

        filmFailReleaseDate = new Film("Name", "Description",
                LocalDate.of(1890, 3, 25), 200, null, null, null, null);

    }

    @Test
    void contextLoads() {
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
        assertThat(userOptional.get().getLogin(), is(user1.getLogin()));
        assertThat(userOptional.get().getName(), is(user1.getName()));
        assertThat(userOptional.get().getEmail(), is(user1.getEmail()));
        assertThat(userOptional.get().getBirthday(), is(user1.getBirthday()));
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

    @Test
    public void testAddFilm() {
        mpa.setId(1);
        genre.setId(2);
        genres.add(genre);

        long filmId = 1L;

        filmStorage.add(film);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(filmId));

        assertTrue(filmOptional.isPresent());
        assertThat(filmOptional.get().getId(), is(filmId));
        assertThat(filmOptional.get().getName(), is(film.getName()));
        assertThat(filmOptional.get().getDescription(), is(film.getDescription()));
        assertThat(filmOptional.get().getReleaseDate(), is(film.getReleaseDate()));
        assertThat(filmOptional.get().getDuration(), is(film.getDuration()));
        assertThat(filmOptional.get().getUserFilmLikes(), is(film.getUserFilmLikes()));
        assertThat(filmOptional.get().getRate(), is(film.getRate()));
    }

    @Test
    public void testFindFilmById() {
        long filmId = 1L;

        filmStorage.add(film);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(filmId));

        assertTrue(filmOptional.isPresent());
        assertThat(filmOptional.get().getId(), is(filmId));
    }

    @Test
    public void testFindAllFilms() {
        filmStorage.add(film);
        filmStorage.add(film2);

        List<Film> filmList = filmStorage.getFilms();

        assertNotNull(filmList);
        assertThat(filmList.size(), equalTo(3));
    }

    @Test
    public void updateFilm() {
        long filmId = 1L;

        Film updateFilm = new Film("Update Super Film2", "Update Super film description2",
                LocalDate.of(1967, 11, 14), 100, 0, mpa, genres, usersFilmsLikes);
        updateFilm.setId(filmId);

        filmStorage.add(film);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(filmId));

        assertTrue(filmOptional.isPresent());
        assertThat(filmOptional.get().getId(), is(filmId));

        filmStorage.update(updateFilm);

        filmOptional = Optional.ofNullable(filmStorage.getFilm(filmId));

        assertTrue(filmOptional.isPresent());
        assertThat(filmOptional.get().getId(), is(filmId));
        assertThat(filmOptional.get().getName(), is("Update Super Film2"));
        assertThat(filmOptional.get().getDescription(), is("Update Super film description2"));
    }

    @Test
    public void addLike() {
        userStorage.add(user1);

        filmStorage.add(film);

        filmStorage.addLike(film.getId(), user1.getId());

        Film film1 = filmStorage.getFilm(4L);

        Set<Long> usersLikesId = film1.getUserFilmLikes();
        Long userId = usersLikesId.stream().filter(data -> Objects.equals(data, 4L)).findFirst().get();

        assertNotNull(usersLikesId);
        assertThat(userId, is(4L));
    }

    @Test
    public void removeLike() {
        userStorage.add(user1);

        filmStorage.add(film);

        filmStorage.addLike(film.getId(), user1.getId());

        Film film1 = filmStorage.getFilm(4L);

        Set<Long> usersLikesId = film1.getUserFilmLikes();
        Long userId = usersLikesId.stream().filter(data -> Objects.equals(data, 4L)).findFirst().get();

        assertNotNull(usersLikesId);
        assertThat(userId, is(4L));

        filmStorage.removeLike(film.getId(), user1.getId());

        usersLikesId = film.getUserFilmLikes();

        assertThat(usersLikesId.size(), is(0));
    }

    @Test
    public void testGetAllGenres() {

        List<Genre> genres = filmStorage.getAllGenres();

        assertNotNull(genres);
        assertThat(genres.size(), is(6));
    }

    @Test
    public void testGetGenresById() {
        Long id = 3L;
        Genre genre = filmStorage.getGenreById(id);

        assertNotNull(genre);
        assertThat(genre.getId(), is(3));
        assertThat(genre.getName(), is("Мультфильм"));
    }

    @Test
    public void testGetAllMPA() {

        List<MPA> mpas = filmStorage.getAllMPA();

        assertNotNull(mpas);
        assertThat(mpas.size(), is(5));
    }

    @Test
    public void testGetMPAById() {
        Long id = 4L;
        MPA mpa = filmStorage.getMPAById(id);

        assertNotNull(mpa);
        assertThat(mpa.getId(), is(4));
        assertThat(mpa.getName(), is("R"));
    }
}
