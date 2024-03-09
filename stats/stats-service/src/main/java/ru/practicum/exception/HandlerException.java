package ru.practicum.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerException {
    private static final Logger log = LoggerFactory.getLogger(HandlerException.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //ошибка 400
    public ErrorResponse handleInvalidParametersException(final InvalidParametersException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //ошибка 400
    public ErrorResponse handleServletRequestBindingException(final ServletRequestBindingException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //ошибка 500
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Произошла непредвиденная ошибка", e);
        return new ErrorResponse("Произошла непредвиденная ошибка");
    }
}
