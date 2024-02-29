package ru.practicum.exception;

public class EventValidationException extends RuntimeException {

    public EventValidationException(final String message) {
        super(message);
    }
}