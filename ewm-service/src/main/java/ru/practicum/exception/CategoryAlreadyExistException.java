package ru.practicum.exception;

public class CategoryAlreadyExistException extends RuntimeException {
    public CategoryAlreadyExistException(final String message) {
        super(message);
    }
}
