package ru.water.account_service.controller;



import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.water.account_service.dto.CreateAccountRequest;
import ru.water.account_service.dto.GetAccountDataResponse;
import ru.water.account_service.logger.Messages;
import ru.water.account_service.service.AccountService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
public class AccountController {

    private final AccountService service;

    @PostMapping("/create-account")
    public ResponseEntity<Void> createAccount(@RequestBody @Valid CreateAccountRequest dto) {
        log.info(Messages.CREATE_ACCOUNT_REQUEST.message);
        service.createAccount(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-account/{id}")
    public ResponseEntity<GetAccountDataResponse> getAccount(@PathVariable Long id) {
        log.info(Messages.GET_ACCOUNT_REQUEST.message);
        return ResponseEntity.ok(service.getAccount(id));
    }

    @GetMapping("/check-account/{userId}")
    public ResponseEntity<Void> getAccountByUserId(@PathVariable @Valid @NotBlank String userId) {
        log.info(Messages.CHECK_ACCOUNT_REQUEST.message);
        service.getAccountByUserId(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-account/{userId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable @Valid @NotBlank String userId) {
        log.info(Messages.DELETE_ACCOUNT_REQUEST.message);
        service.deleteAccount(userId);
        return ResponseEntity.noContent().build();
    }
}
