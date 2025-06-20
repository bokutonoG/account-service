package ru.water.account_service.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public enum Messages {
    USER_NAME_ALREADY_EXISTS("Аккаунт с таким user_id уже существует"),

    CREATE_ACCOUNT_REQUEST("Получен запрос POST /api/v1/create-account"),
    GET_ACCOUNT_REQUEST("Получен запрос GET /api/v1/get-account"),
    CHECK_ACCOUNT_REQUEST("Получен запрос GET /api/v1/check-account");

    public final String message;
}
