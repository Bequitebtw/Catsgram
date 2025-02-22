package ru.yandex.practicum.catsgram.exception;

public class ParameterParseException extends NumberFormatException {
    public ParameterParseException(String message) {
        super(message);
    }
}
