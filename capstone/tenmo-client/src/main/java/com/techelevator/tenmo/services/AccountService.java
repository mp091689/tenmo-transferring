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
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

//    public BigDecimal getBalance(int id){
//        return restTemplate.getForObject(API_BASE_URL + "accounts/" + id, BigDecimal.class);
//    }
    public BigDecimal getBalance(AuthenticatedUser auth){
        BigDecimal balance = BigDecimal.valueOf(0);
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "balance/" + auth.getUser().getId(), HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
