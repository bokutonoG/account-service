package ru.water.account_service.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.water.account_service.dto.CreateAccountRequest;
import ru.water.account_service.entity.Account;
import ru.water.account_service.exception.AccountNotFoundException;
import ru.water.account_service.repository.AccountRepository;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final ObjectMapper mapper;


    public ResponseEntity<Account> getAccount(Long id) {
        var account = repository.findById(id);
        return account.map(v -> ResponseEntity.ok().body(v))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public void getAccountByUserId(String userId) {
        repository.findAccountByUserId(userId)
                .orElseThrow(() -> new AccountNotFoundException("аккаунт не найден"));


    }

    @Transactional
    public ResponseEntity<Void> createAccount(CreateAccountRequest dto) {
        var account = mapper.convertValue(dto, Account.class);
        repository.save(account);
        return ResponseEntity.ok().build();
    }
}
