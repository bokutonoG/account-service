package ru.water.account_service.controller;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.water.account_service.dto.CreateAccountRequest;
import ru.water.account_service.dto.GetAccountDataResponse;
import ru.water.account_service.service.AccountService;

@RestController
@RequestMapping("/api/v1")

public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping("/create-account")
    public ResponseEntity<Void> createAccount(@RequestBody CreateAccountRequest dto) {
        service.createAccount(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-account/{id}")
    public ResponseEntity<GetAccountDataResponse> getAccount(@PathVariable Long id) {

        return ResponseEntity.ok(service.getAccount(id));
    }

    @GetMapping("/get-account/by-user-id/{userId}")
    public ResponseEntity<Void> getAccountByUserId(@PathVariable String userId) {
        service.getAccountByUserId(userId);
        return ResponseEntity.ok().build();
    }
}
