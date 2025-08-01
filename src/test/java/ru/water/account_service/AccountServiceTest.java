package ru.water.account_service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import ru.water.account_service.dto.CreateAccountRequest;
import ru.water.account_service.entity.Account;
import ru.water.account_service.exception.AccountAlreadyExistsException;
import ru.water.account_service.exception.AccountNotFoundException;
import ru.water.account_service.exception.InternalServerErrorException;
import ru.water.account_service.repository.AccountRepository;
import ru.water.account_service.service.AccountService;


import java.util.Optional;

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
            assertThrows(InternalServerErrorException.class, ()-> service.createAccount(dto));

            verify(mapper).convertValue(dto, Account.class);
            verify(repository, never()).save(any());
        }

        @Test
        void createAccountWithDuplicateUserId() {
            //given
            var dto = getCreateAccountRequest();
            when(repository.existsByUserId(dto.userId())).thenReturn(true);
            // when + then
            assertThrows(AccountAlreadyExistsException.class, ()-> service.createAccount(dto));
            // then
            verify(repository).existsByUserId(dto.userId());
            verify(mapper, never()).convertValue(any(), eq(Account.class));
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
    @DisplayName("получение аккаунта по id")
    class getAccountTests {
        @Test
        public void getAccountPositiveTest() {
            //given
            var account = getAccount();
            Long id = 1L;
            when(repository.findById(id)).thenReturn(Optional.of(account));
            //when
            var response = service.getAccount(1L);
            //then
            assertEquals(account.getName(), response.name());
            assertEquals(account.getUserId(), response.userId());
            verify(repository).findById(id);
        }
        @Test
        public void getAccountMappingExceptionTest() {
            //given
            var account = getAccount();
            Long id = 1L;
            when(repository.findById(id)).thenReturn(Optional.empty());
            //when
            assertThrows(AccountNotFoundException.class, () -> service.getAccount(1L));
            //then
            verify(repository).findById(id);
        }
        @Test
        public void getAccountInternalExceptionTest() {
            //given
            Long id = 1L;
            when(repository.findById(id)).thenThrow(new DataAccessResourceFailureException("DB problem"));
            //when
            assertThrows(InternalServerErrorException.class, () -> service.getAccount(1L));
            //then
            verify(repository).findById(id);
        }


    }

    @Nested
    @DisplayName("проверка наличия аккаунта по user_id")
    class getAccountByUserId {

        @Test
        void getAccountByUserIdPositiveTest() {
            //given
            var account = getAccount();
            when(repository.findAccountByUserId(DEFAULT_USER_ID)).thenReturn(Optional.of(account));
            //when
            service.getAccountByUserId(DEFAULT_USER_ID);
            //then
            verify(repository).findAccountByUserId(DEFAULT_USER_ID);
        }

        @Test
        void getAccountByUserIdNegativeTest() {
            //given
            when(repository.findAccountByUserId(DEFAULT_USER_ID)).thenReturn(Optional.empty());
            //when
            assertThrows(AccountNotFoundException.class, () -> service.getAccountByUserId(DEFAULT_USER_ID));
            //then
            verify(repository).findAccountByUserId(DEFAULT_USER_ID);
        }
    }


    private static Account getAccount() {
        return new Account(
                1L,
                DEFAULT_USER_ID,
                VALID_NAME
        );
    }
    private static CreateAccountRequest getCreateAccountRequest() {
        return CreateAccountRequest.builder()
                .name(VALID_NAME)
                .userId(DEFAULT_USER_ID)
                .build();
    }

}
