package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.client.AuthorizationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthorizationService {

    private final AuthorizationClient authorizationClient;

    @Autowired
    public AuthorizationService(AuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    public boolean isAuthorizedTransaction(){
        var response = authorizationClient.isAuthorizedTransaction();

        if (response.getStatusCode().isError()) {
            throw new UnsupportedOperationException("Transaction not authorized.");
        }

        return response.getBody().data().authorization();
    }
}
