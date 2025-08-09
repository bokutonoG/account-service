package ru.water.account_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.water.account_service.entity.Account;
import ru.water.account_service.repository.AccountRepository;
import ru.water.account_service.service.AccountService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig({AccountService.class})
public class AccountServiceImplTest {

    private static final Long ACCOUNT_ID = 1L;
    private static final String DEFAULT_USER_ID = "1234";
    private static final String VALID_NAME = "Ann";

    @Autowired
    private AccountService accountService;

    @MockitoBean
    private AccountRepository repository;

    @MockitoBean
    private ObjectMapper mapper;

    @Test
    void testPositiveGetAccount() {
        //given
        Account account = getTestAccount();
        when(repository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        //when
        var resp = accountService.getAccount(ACCOUNT_ID);

        verify(repository).findById(eq(ACCOUNT_ID));
        assertEquals(account.getName(), resp.name());
        assertEquals(account.getUserId(), resp.userId());

    }

    private static Account getTestAccount() {
        return new Account(
                1L,
                DEFAULT_USER_ID,
                VALID_NAME
        );
    }
}


