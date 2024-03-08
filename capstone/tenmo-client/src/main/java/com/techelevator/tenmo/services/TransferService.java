package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.http.server.reactive.HttpHeadResponseDecorator;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TransferService {

public static final String API_BASE_URL = "http://localhost:8080/transfers";

private RestTemplate restTemplate = new RestTemplate();

private String authToken = null;
private AuthenticatedUser user;

public void setAuthToken(String authToken) {
    this.authToken = authToken;
}
    public Transfer getById(int id){
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + id, HttpMethod.GET, makeAuthEntity(), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
    }
        return transfer;
    }

    public Transfer[] getAll() {
    Transfer[] transferList = null;
    String type;
    String name;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transferList = response.getBody();
            System.out.println("-------------------------------------------\n" +
                    "Transfers\n" +
                    "ID          From/To                 Amount\n" +
                    "-------------------------------------------");
            if (transferList != null) {
                for (Transfer t : transferList) {
                    if (user.getUser().getId() != t.getId())
                    {
                        type = "To :";
                        name = t.getUserTo();
                    }
                    else
                    {
                        type = "From: ";
                        name = t.getUserTo();
                    }
                    System.out.println(t.getId() + "" + type + name + "" + t.getAmount());
                }
                System.out.println("---------\n" +
                        "Please enter transfer ID to view details (0 to cancel): \"");
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferList;
    }

    public List<Transfer> getPending(){
    List<Transfer> pendingList = new ArrayList<>();
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/pending", HttpMethod.GET, makeAuthEntity(), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return pendingList;
    }



    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer){
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(authToken);
    return new HttpEntity<>(transfer, headers);
    }

}
