package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService {
    private final String SERVICE_API_URL;
    private final RestTemplate restTemplate;
    private AuthenticatedUser user;

    public UserService(String baseUrl, RestTemplate restTemplate) {
        this.SERVICE_API_URL = baseUrl + "/users";
        this.restTemplate = restTemplate;
    }

    public void setAuthenticatedUser(AuthenticatedUser user) {
        this.user = user;
    }

    public List<User> getAllUsers() {
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(SERVICE_API_URL, HttpMethod.GET, makeAuthEntity(), User[].class);
            if (response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return new ArrayList<>();
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }
}
