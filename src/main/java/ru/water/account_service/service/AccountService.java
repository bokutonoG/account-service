package ru.water.account_service.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.water.account_service.dto.CreateAccountRequest;
import ru.water.account_service.dto.GetAccountDataResponse;
import ru.water.account_service.entity.Account;
import ru.water.account_service.exception.AccountAlreadyExistsException;
import ru.water.account_service.exception.AccountNotFoundException;
import ru.water.account_service.exception.InternalServerErrorException;
import ru.water.account_service.logger.Messages;
import ru.water.account_service.repository.AccountRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final ObjectMapper mapper;

    public GetAccountDataResponse getAccount(Long id) {
        try {
            var account = repository.findById(id);
            return account.map(v -> GetAccountDataResponse.builder()
                            .userId(v.getUserId())
                            .name(v.getName())
                            .build())
                            .orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));
        } catch (DataAccessException e) {
            throw new InternalServerErrorException("Ошибка при обращении к БД", e);
        }
    }

    public void getAccountByUserId(String userId) {
        repository.findAccountByUserId(userId)
                  .orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден"));
    }

    @Transactional
    public void createAccount(CreateAccountRequest dto) {
        log.info("Начинаем создавать аккаунт с user_id={}", dto.userId());
        if(repository.existsByUserId(dto.userId())) {
            throw new AccountAlreadyExistsException(Messages.USER_NAME_ALREADY_EXISTS.getMessage());
        }
        try {
            var account = mapper.convertValue(dto, Account.class);
            repository.save(account);
            log.info("Аккаунт успешно создан: userId={}", dto.userId());
        }
        catch (IllegalArgumentException | DataAccessException e) {
            throw new InternalServerErrorException("Ошибка при сохранении аккаунта", e);
        }

    }
}
