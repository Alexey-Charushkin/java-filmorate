package ru.yandex.practicum.filmorate.exceptions;

import java.sql.SQLException;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

