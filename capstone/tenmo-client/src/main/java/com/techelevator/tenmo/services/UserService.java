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

public class UserService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user;

    public void setAuthenticatedUser(AuthenticatedUser user) {
        this.user = user;
    }

    public void getAllAccounts() {
        User[] accounts = new User[0];
        try {
            System.out.println("-------------------------------------------\n" +
                    "Users\n" +
                    "ID          Name\n" +
                    "-------------------------------------------");
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "user", HttpMethod.GET, makeAuthEntity(), User[].class);
            accounts = response.getBody();
            if (accounts != null) {
                for (User u : accounts) {
                    if (user.getUser().getId() != u.getId()) {
                        System.out.println(u.getId() + "\t" + u.getUsername());
                    }
                }
            }
            else
            {
                System.out.println("There are no registered accounts.");
            }
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

        private HttpEntity<Void> makeAuthEntity(){
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(user.getToken());
            return new HttpEntity<>(headers);
        }
}
