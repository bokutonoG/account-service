package ru.water.account_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.water.account_service.controller.AccountController;
import ru.water.account_service.dto.CreateAccountRequest;
import ru.water.account_service.dto.GetAccountDataResponse;
import ru.water.account_service.exception.AccountAlreadyExistsException;
import ru.water.account_service.exception.AccountNotFoundException;
import ru.water.account_service.exception.InternalServerErrorException;
import ru.water.account_service.handler.GlobalExceptionHandler;
import ru.water.account_service.service.AccountService;


import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({AccountController.class, GlobalExceptionHandler.class})
public class AccountControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private AccountService accountService;

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String GET_ACCOUNT_PATH = "/api/v1/get-account/{id}";
    private final String CREATE_ACCOUNT_PATH = "/api/v1/create-account";
    private final String CHECK_ACCOUNT_PATH = "/api/v1/check-account/{userId}";

    @Test
    @SneakyThrows
    @DisplayName("Позитивный тест на GET /v1/get-account/{id}")
    void testPositiveGetAccount() {
        Long id = 1L;
        when(accountService.getAccount(id)).thenReturn(GetAccountDataResponse.builder()
                        .userId("12345")
                        .name("gleb")
                        .build());
        mvc.perform(MockMvcRequestBuilders
                        .get(GET_ACCOUNT_PATH, id)
                        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user_id").value("12345"))
            .andExpect(jsonPath("$.name").value("gleb"));
    }

    @Test
    @SneakyThrows
    @DisplayName("Негативный тест на GET /v1/get-account/{id}, пользователь не найден")
    void testNegativeGetAccount() {
        Long id = 1L;
        var expectedResponseBody = "{\"code\":\"AS-004\",\"message\":\"Аккаунт не найден\"}";
        when(accountService.getAccount(id)).thenThrow(AccountNotFoundException.class);
        mvc.perform(MockMvcRequestBuilders
                .get(GET_ACCOUNT_PATH, id)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().json(expectedResponseBody));
    }

    @Test
    @SneakyThrows
    @DisplayName("Негативный тест на GET /v1/get-account/{id}, ошибка обращения к бд")
    void testGetAccountExceptionDuringDbCommunicate() {
        Long id = 1L;
        var expectedResponseBody = "{\"code\":\"AS-002\",\"message\":\"Внутрення ошибка сервера\"}";
        when(accountService.getAccount(id)).thenThrow(InternalServerErrorException.class);

        mvc.perform(MockMvcRequestBuilders
                .get(GET_ACCOUNT_PATH, id)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(expectedResponseBody));
    }

    @ParameterizedTest
    @MethodSource("emptyValues")
    @SneakyThrows
    @DisplayName("Невалидный user_id при создании аккаунта")
    void testCreateAccountInvalidUserId(String userId) {

        var expectedResponseBody = "{\"code\":\"AS-001\",\"message\":\"Неправильный запрос\"}";
        var dto = new CreateAccountRequest(userId, "gleb");

        mvc.perform(MockMvcRequestBuilders
                .post(CREATE_ACCOUNT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponseBody));

        verifyNoInteractions(accountService);
    }
    @ParameterizedTest
    @MethodSource("emptyValues")
    @SneakyThrows
    @DisplayName("Невалидный name при создании аккаунта")
    void testCreateAccountInvalidName(String name) {

        var expectedResponseBody = "{\"code\":\"AS-001\",\"message\":\"Неправильный запрос\"}";
        var dto = new CreateAccountRequest("123", name);

        mvc.perform(MockMvcRequestBuilders
                        .post(CREATE_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsBytes(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponseBody));

        verifyNoInteractions(accountService);
    }

    @Test
    @SneakyThrows
    @DisplayName("Позитивный тест на POST /api/v1/create-account")
    void testPositiveCreateAccount() {
        // создали дто как тело для имитации запроса
        var dto = new CreateAccountRequest("105068", "gleb");
        // мокать поведение сервиса не нужно ибо createAccount ничего не возвращает
        mvc.perform(MockMvcRequestBuilders
                .post(CREATE_ACCOUNT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(dto))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(accountService).createAccount(dto);
    }

    @Test
    @SneakyThrows
    @DisplayName("")
    void testNegativeCreateAccountExceptionDuringSaving() {
        var requestBody = new CreateAccountRequest("105068", "gleb");
        var expectedResponseBody = "{\"code\":\"AS-002\",\"message\":\"Внутрення ошибка сервера\"}";
        // когда нужно чтоб при вызове мока метод которого void вернул исключение
        doThrow(new InternalServerErrorException("Ошибка", new RuntimeException()))
                .when(accountService).createAccount(any());

        mvc.perform(MockMvcRequestBuilders
                .post(CREATE_ACCOUNT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(requestBody))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(expectedResponseBody));

        verify(accountService).createAccount(any());
    }

    @Test
    @SneakyThrows
    @DisplayName("")
    void testNegativeCreateAccountThatAlreadyExists() {
        var requestBody = new CreateAccountRequest("105068", "gleb");
        var expectedResponseBody = "{\"code\":\"AS-003\",\"message\":\"Такой аккаунт уже есть\"}";
        // когда нужно чтоб при вызове мока метод которого void вернул исключение
        doThrow(new AccountAlreadyExistsException("Аккаунт уже есть"))
                .when(accountService).createAccount(any());

        mvc.perform(MockMvcRequestBuilders
                        .post(CREATE_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsBytes(requestBody))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAlreadyReported())
                .andExpect(content().json(expectedResponseBody));

        verify(accountService).createAccount(any());
    }

    @Test
    @SneakyThrows
    @DisplayName("")
    void testPositiveGetAccountByUserId() {
        // в запросе нужен user_id
        var userId = "13456";
        // метод ничего не возвращает поэтому поведение ему задавать не надо
        // начинаем собирать запрос
        mvc.perform(MockMvcRequestBuilders
                .get(CHECK_ACCOUNT_PATH, userId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(accountService).getAccountByUserId(userId);
    }

    @Test
    @SneakyThrows
    @DisplayName("")
    void testNegativeGetAccountByUserId() {
        // в запросе нужен user_id
        var userId = "13456";
        // здесь в ответ прийдет json, поэтому соберем его
        var expectedResponseBody = "{\"code\":\"AS-004\",\"message\":\"Аккаунт не найден\"}";
        // замокаем поведение так как здесь нужно получить от сервиса исключение
        doThrow(new AccountNotFoundException("Аккаунт не найден"))
                .when(accountService).getAccountByUserId(userId);
        // начинаем собирать запрос
        mvc.perform(MockMvcRequestBuilders
                        .get(CHECK_ACCOUNT_PATH, userId)
                        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().json(expectedResponseBody));

        verify(accountService).getAccountByUserId(userId);
    }

    public static Stream<Arguments> emptyValues() {
        return Stream.of(
                Arguments.of("     "),
                Arguments.of((String)null),
                Arguments.of("")
        );
    }
}
