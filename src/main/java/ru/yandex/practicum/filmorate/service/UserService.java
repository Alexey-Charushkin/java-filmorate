package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDaoStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final Validate validator;
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

//    private final UserDaoStorage userDaoStorage;

    private Long id = 0L;

    public User create(User user) {
        validator.validate(user);
        userStorage.add(user);
        return user;
    }

    public ResponseEntity<?> update(User user) {
        validator.userIsPresent(user.getId());
        validator.validate(user);
        userStorage.update(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public List<User> findAll() {
        log.info("Текущее количество пользователей: {}", userStorage.getUsers().size());
        return userStorage.getUsers();
    }

    public User findById(Long id) {
        validator.userIsPresent(id);
        return userStorage.findUserById(id);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.findUserById(userId);
        User friendUser = userStorage.findUserById(friendId);
        log.info("Пользователь {} добавлен в друзья к пользователю {}.", user, friendUser);
        userStorage.addFriend(userId, friendId);
        // user.setUserFriendsId(friendId);
        //friendUser.setUserFriendsId(userId);
        return friendUser;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = userStorage.findUserById(userId);
        User friendUser = userStorage.findUserById(friendId);
        log.info("Пользователь {} удалён из друзей пользователя {}.", user, friendUser);
        user.removeFriends(friendUser);
        friendUser.removeFriends(user);
        return friendUser;
    }

    public void removeUserById(Long userId) {
        validator.userIsPresent(userId);
        userStorage.remove(userId);
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.findUserById(userId);
        List<User> userFriends = new ArrayList<>();
        if (user.getUserFriendsId().size() == 0) {
            log.info("Список друзей пользователя {} пуст.", user.getName());
        }
        Set<Long> friendsId = new HashSet<>(user.getUserFriendsId());
        for (Long id : friendsId) {
            userFriends.add(userStorage.findUserById(id));
        }
        log.info("Количество друзей пользователя {} : {}.", user.getName(), userFriends.size());
        return userFriends;
    }

    public List<User> getСommonFriends(Long id, Long friendId) {
        User user = userStorage.findUserById(id);
        User userFriend = userStorage.findUserById(friendId);
        if (user.getUserFriendsId().size() == 0) {
            log.info("Список друзей пользователя {} пуст.", user.getName());
        }
        if (userFriend.getUserFriendsId().size() == 0) {
            log.info("Список друзей пользователя {} пуст.", user.getName());
        }
        Set<Long> friendsIdUser = new HashSet<>(user.getUserFriendsId());
        Set<Long> friendsIdFriend = new HashSet<>(userFriend.getUserFriendsId());


        log.info("Список общих друзей пользователя {} и пользователя {} получен.",
                user.getName(), userFriend.getName());
        return friendsIdUser.stream()
                .filter(friendsIdFriend::contains)
                .map(userId -> userStorage.findUserById(userId))
                .collect(Collectors.toList());
    }

}