package ru.water.account_service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.water.account_service.dto.CreateAccountRequest;
import ru.water.account_service.entity.Account;
import ru.water.account_service.repository.AccountRepository;
import ru.water.account_service.service.AccountService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    private static final String DEFAULT_USER_ID = "1234";
    private static final String VALID_NAME = "Ann";

    @Mock
    private AccountRepository repository;
    @Mock
    private ObjectMapper mapper;
    @InjectMocks
    private AccountService service;

    @Nested
    @DisplayName("создание аккаунта")
    class createAccountTests {
        @Test
        void createAccountPositiveTest() {
            // given
            var dto = getCreateAccountRequest();
            var account = getAccount();
            when(mapper.convertValue(dto, Account.class)).thenReturn(account);
            when(repository.save(account)).thenReturn(account);
            // when
            service.createAccount(dto);
            //then
            verify(mapper).convertValue(dto, Account.class);
            verify(repository).save(account);

        }
        @Test
        void createAccountNegativeMapperExceptionTest() {
            //given
            var dto = getCreateAccountRequest();
            when(mapper.convertValue(dto, Account.class)).thenThrow(IllegalArgumentException.class);
            // when + then
            assertThrows(IllegalArgumentException.class, ()-> service.createAccount(dto));

            verify(mapper).convertValue(dto, Account.class);
            verify(repository, never()).save(any());
        }

        @Test
        void createAccountNegativeRepoExceptionTest() {
            //given
            var dto = getCreateAccountRequest();
            var account = getAccount();

            when(mapper.convertValue(dto, Account.class)).thenReturn(account);
            when(repository.save(account)).thenThrow(RuntimeException.class);
            // when + then
            assertThrows(RuntimeException.class, () -> service.createAccount(dto));

            verify(mapper).convertValue(dto, Account.class);
            verify(repository).save(account);

        }
    }

    @Nested
    @DisplayName("")
    class getAccountTests {


    }


    private static Account getAccount() {
        return new Account(
                1L,
                DEFAULT_USER_ID,
                VALID_NAME
        );
    }
    private static CreateAccountRequest getCreateAccountRequest() {
        return new CreateAccountRequest(
                DEFAULT_USER_ID,
                VALID_NAME
        );
    }

}
