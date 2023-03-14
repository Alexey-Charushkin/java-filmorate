package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDaoStorage extends UserStorage {
    List<Long> findUserFriendsById(Long idUser);
    boolean isFriendExist(Long userId, Long friendId);
}
