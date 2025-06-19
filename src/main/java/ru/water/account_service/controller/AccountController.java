package ru.water.account_service.controller;



import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.water.account_service.dto.CreateAccountRequest;
import ru.water.account_service.dto.GetAccountDataResponse;
import ru.water.account_service.service.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
public class AccountController {

    private final AccountService service;


    @PostMapping("/create-account")
    public ResponseEntity<Void> createAccount(@RequestBody @Valid CreateAccountRequest dto) {
        service.createAccount(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-account/{id}")
    public ResponseEntity<GetAccountDataResponse> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAccount(id));
    }

    @GetMapping("/get-account/by-user-id/{userId}")
    public ResponseEntity<Void> getAccountByUserId(@PathVariable @Valid @NotBlank String userId) {
        service.getAccountByUserId(userId);
        return ResponseEntity.ok().build();
    }
}
