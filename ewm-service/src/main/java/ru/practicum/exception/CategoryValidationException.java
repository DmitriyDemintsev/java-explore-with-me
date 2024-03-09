package ru.practicum.exception;

public class CategoryValidationException extends RuntimeException {
    public CategoryValidationException(final String message) {
        super(message);
    }
}
