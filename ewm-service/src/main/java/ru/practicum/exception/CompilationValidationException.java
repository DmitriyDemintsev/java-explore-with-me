package ru.practicum.exception;

public class CompilationValidationException extends RuntimeException {
    public CompilationValidationException(final String message) {
        super(message);
    }
}
