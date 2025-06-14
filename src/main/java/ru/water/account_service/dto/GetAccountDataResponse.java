package ru.water.account_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record GetAccountDataResponse(@JsonProperty("user_id") String userId, String name) {
}
