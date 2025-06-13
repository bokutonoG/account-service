package ru.water.account_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.water.account_service.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByUserId(String userId);
}
