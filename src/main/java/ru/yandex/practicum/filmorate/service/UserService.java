package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EmptyUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Log4j2
@Service
public class UserService {

    private Validate validator;
    private UserStorage userStorage;

    UserService(Validate validator, UserStorage userStorage) {
        this.validator = validator;
        this.userStorage = userStorage;
    }

    private Long id = 0L;

    public User create(User user) {
        validator.validate(user);
        user.setId(++id);
        log.info("Пользователь добавлен {}.", user);
        userStorage.add(user);
        return user;
    }

    public ResponseEntity<?> update(User user) {
        userIsPresent(user.getId());
        validator.validate(user);
        log.info("Пользователь обновлён {}.", user);
        userStorage.update(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public User remove() {
        return null;
    }

    public List<User> findAll() {
        log.info("Текущее количество пользователей: {}", userStorage.getUsers().size());
        return new ArrayList<>(userStorage.getUsers().values());
    }

    public User findById(Long id) {

           userIsPresent(id);
        log.info("Пользователь с id {} найден.", id);
        return userStorage.findUserById(id);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = userIsPresent(userId);
        User friendUser = userIsPresent(friendId);
        log.info("Пользователь {} добавлен в друзья к пользователю {}.", user, friendUser);
        user.setUserFriendsId(friendId);
        userStorage.add(user);
        friendUser.setUserFriendsId(userId);
        userStorage.add(friendUser);
        return friendUser;
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userIsPresent(userId);
        User friendUser = userIsPresent(friendId);
        log.info("Пользователь {} удалён из друзей пользователя {}.", user, friendUser);
        user.removeFriends(friendUser);
        userStorage.add(user);
        friendUser.removeFriends(user);
        userStorage.add(friendUser);
    }

    public List<User> getFriends(Long userId) {
        User user = userIsPresent(userId);
        List<User> userFriends = new ArrayList<>();
        if(user.getUserFriendsId().size() == 0) {
            log.info("Список друзей пользователя {} пуст.", user.getName());
        }
        Set<Long> friendsId = new HashSet<>(user.getUserFriendsId());
        for(Long id: friendsId) {
            userFriends.add(userStorage.findUserById(id));
        }
        return userFriends;
    }

    private User userIsPresent(Long id) {
        Optional<User> isUser = Optional.ofNullable(userStorage.findUserById(id));
        if (!isUser.isPresent()) {
            log.warn("Пользователь c id {} отсутствует в базе.", id);
            throw new EmptyUserException("Пользователь с id " + id
                    + " отсутствует в базе.");
        }
        return isUser.get();
    }

}