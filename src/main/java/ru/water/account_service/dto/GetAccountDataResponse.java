package ru.water.account_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetAccountDataResponse(@JsonProperty("user_id") String userId, String name) {
}
