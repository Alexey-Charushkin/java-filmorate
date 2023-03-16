package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class ErrorResponse {
    String description;
    public ErrorResponse(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
