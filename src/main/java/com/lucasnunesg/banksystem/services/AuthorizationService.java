package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.client.AuthorizationClient;
import com.lucasnunesg.banksystem.client.dto.AuthorizationResponseDto;
import com.lucasnunesg.banksystem.exceptions.ExternalServiceUnavailableException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class AuthorizationService {

    private final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
    private final AuthorizationClient authorizationClient;

    @Autowired
    public AuthorizationService(AuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    public boolean isAuthorizedTransaction(){
        try {
            ResponseEntity<AuthorizationResponseDto> response = authorizationClient.isAuthorizedTransaction();

            logger.info("External authorization API response body: {}", response.getBody());
            return response.getBody().data().authorization();
        } catch (FeignException e) {
            if (e.status() == HttpStatus.FORBIDDEN.value()) {
                logger.error("Transaction unauthorized by external service");
                return false;
            }
            logger.error("Unknown Feign Error");
            throw new ExternalServiceUnavailableException("External authorization system is not responding: " + e.getMessage());
        } catch (Exception e) {
            logger.error("External authorization service failed");
            throw new ExternalServiceUnavailableException("External authorization system is not responding: " + e.getMessage());
        }
    }
}
