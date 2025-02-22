package ru.yandex.practicum.catsgram.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
}
