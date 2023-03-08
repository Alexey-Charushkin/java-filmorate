package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

public class FilmNotFoundException extends RuntimeException {
   public FilmNotFoundException(String message) { super(message); }
}
