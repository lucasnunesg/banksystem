package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.client.AuthorizationClient;
import com.lucasnunesg.banksystem.client.dto.AuthorizationResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    AuthorizationClient client;

    @InjectMocks
    AuthorizationService service;

    @Test
    void isAuthorizedTransactionShouldReturnCorrectlyWhenRequestSucceeds() {
        AuthorizationResponseDto.Data data = new AuthorizationResponseDto.Data(true);
        AuthorizationResponseDto dto = new AuthorizationResponseDto("200", data);

        ResponseEntity<AuthorizationResponseDto> response = new ResponseEntity<>(dto, OK);

        when(client.isAuthorizedTransaction()).thenReturn(response);
        boolean result = service.isAuthorizedTransaction();

        assertTrue(result);
    }

    @Test
    void testIsAuthorizedTransactionException() {

        when(client.isAuthorizedTransaction()).thenThrow(new RuntimeException("message"));

        assertThrows(RuntimeException.class, () -> {
            service.isAuthorizedTransaction();
        });
    }


}