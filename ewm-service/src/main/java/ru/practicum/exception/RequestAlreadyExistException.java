package ru.practicum.exception;

public class RequestAlreadyExistException extends RuntimeException {
    public RequestAlreadyExistException(final String message) {
        super(message);
    }
}
