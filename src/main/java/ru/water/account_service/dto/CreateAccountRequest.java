package ru.water.account_service.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateAccountRequest(@NotBlank String userId,
                                   @NotBlank String name) {
}
