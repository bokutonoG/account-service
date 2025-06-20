package ru.water.account_service.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.water.account_service.enums.ErrorAs;
import ru.water.account_service.enums.ErrorType;
import ru.water.account_service.exception.AccountAlreadyExistsException;
import ru.water.account_service.exception.AccountNotFoundException;
import ru.water.account_service.exception.InternalServerErrorException;
import ru.water.account_service.logger.Messages;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorType handleAccountNotFoundException(AccountNotFoundException e) {
        log.error(e.getMessage(), e);
        return ErrorAs.AS_004.getErrorType();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorType handleNotValidExceptionException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ErrorAs.AS_001.getErrorType();
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
    public ErrorType handleAccountAlreadyExistsException(AccountAlreadyExistsException e) {
        log.error(Messages.USER_NAME_ALREADY_EXISTS.getMessage(), e);
        return ErrorAs.AS_003.getErrorType();
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorType handleInternalServerErrorException(InternalServerErrorException e) {
        log.error(e.getMessage(), e);
        return ErrorAs.AS_002.getErrorType();
    }


}
