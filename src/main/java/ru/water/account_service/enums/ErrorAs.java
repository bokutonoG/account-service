package ru.water.account_service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorAs {

    AS_001(new ErrorType("AS-001", "Неправильный запрос")),
    AS_002(new ErrorType("AS-002", "Внутрення ошибка сервера")),
    AS_003(new ErrorType("AS-003", "Такой аккаунт уже есть")),
    AS_004(new ErrorType("AS-004", "Аккаунт не найден"))
    ;

    private final ErrorType errorType;
}
