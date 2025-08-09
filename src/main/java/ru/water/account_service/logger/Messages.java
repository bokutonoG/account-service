package ru.water.account_service.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Messages {
    USER_NAME_ALREADY_EXISTS("Аккаунт с таким user_id уже существует"),

    CREATE_ACCOUNT_REQUEST("Получен запрос POST /api/v1/create-account"),
    GET_ACCOUNT_REQUEST("Получен запрос GET /api/v1/get-account"),
    CHECK_ACCOUNT_REQUEST("Получен запрос GET /api/v1/check-account"),
    DELETE_ACCOUNT_REQUEST("Получен запрос DELETE /api/v1/delete-account"),

    CREATE_ACCOUNT_RESPONSE("POST /api/v1/create-account успешно обработан"),
    GET_ACCOUNT_RESPONSE("GET /api/v1/get-account успешно обработан"),
    CHECK_ACCOUNT_RESPONSE("GET /api/v1/check-account успешно обработан"),
    DELETE_ACCOUNT_RESPONSE("DELETE /api/v1/delete-account успешно обработан");

    public final String message;
}
