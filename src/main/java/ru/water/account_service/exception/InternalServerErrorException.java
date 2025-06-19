package ru.water.account_service.exception;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
