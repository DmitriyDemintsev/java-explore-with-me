package ru.practicum.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerException {
    private static final Logger log = LoggerFactory.getLogger(HandlerException.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //ошибка 400
    public ErrorResponse handleBindException(final BindException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //ошибка 400
    public ErrorResponse handleServletRequestBindingException(final ServletRequestBindingException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) //ошибка 409
    public ErrorResponse handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //ошибка 400
    public ErrorResponse handleCategoryValidationException(final CategoryValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //ошибка 400
    public ErrorResponse handleCommentValidationException(final CommentValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //ошибка 400
    public ErrorResponse handleCompilationValidationException(final CompilationValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //ошибка 400
    public ErrorResponse handleEventValidationException(final EventValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //ошибка 400
    public ErrorResponse handleRequestValidationException(final RequestValidationException e) {
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //ошибка 400
    public ErrorResponse handleUserValidationException(final UserValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //ошибка 404
    public ErrorResponse handleCategoryNotFoundException(final CategoryNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //ошибка 404
    public ErrorResponse handleCompilationNotFoundException(final CompilationNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //ошибка 404
    public ErrorResponse handleEventNotFoundException(final EventNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //ошибка 404
    public ErrorResponse handleRequestNotFoundException(final RequestNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //ошибка 404
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) //ошибка 409
    public ErrorResponse handleCategoryAlreadyExistException(final CategoryAlreadyExistException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) //ошибка 409
    public ErrorResponse handleEventAlreadyExistException(final EventAlreadyExistException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) //ошибка 409
    public ErrorResponse handleRequestAlreadyExistException(final RequestAlreadyExistException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) //ошибка 409
    public ErrorResponse handleUserAlreadyExistException(final UserAlreadyExistException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //ошибка 500
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Произошла непредвиденная ошибка", e);
        return new ErrorResponse("Произошла непредвиденная ошибка");
    }
}
