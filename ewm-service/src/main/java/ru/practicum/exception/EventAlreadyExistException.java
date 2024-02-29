package ru.practicum.exception;

public class EventAlreadyExistException extends RuntimeException {
    public EventAlreadyExistException(final String message) {
        super(message);
    }
}