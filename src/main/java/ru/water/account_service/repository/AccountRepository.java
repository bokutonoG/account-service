package ru.water.account_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.water.account_service.entity.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByUserId(String userId);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);
}
