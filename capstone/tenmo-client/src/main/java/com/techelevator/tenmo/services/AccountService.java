package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private final String SERVICE_API_URL;
    private final RestTemplate restTemplate;
    private AuthenticatedUser user;

    public AccountService(String baseUrl, RestTemplate restTemplate) {
        SERVICE_API_URL = baseUrl + "/accounts";
        this.restTemplate = restTemplate;
    }

    public void setAuthenticatedUser(AuthenticatedUser user) {
        this.user = user;
    }


    public BigDecimal getBalance() {
        BigDecimal balance = BigDecimal.valueOf(0);
        try {
            balance = getAccount().getBalance();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public Account getAccount() {
        Account account = null;
        try {
            ResponseEntity<Account> response = restTemplate.exchange(SERVICE_API_URL, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

}
