package ru.water.account_service.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.water.account_service.exception.AccountNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleAccountNotFoundException(AccountNotFoundException e) {
        return e.getMessage();
    }
}
