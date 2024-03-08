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
import java.util.Scanner;

public class TransferService {

public static final String API_BASE_URL = "http://localhost:8080/transfers/";

private RestTemplate restTemplate = new RestTemplate();

private String authToken = null;
private AuthenticatedUser user;
public void setAuthenticatedUser(AuthenticatedUser user) {
    this.user = user;
}

public void setAuthToken(String authToken) {
    this.authToken = authToken;
}
//    public Transfer getById(int id){
//        Transfer transfer = null;
//        try {
//            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + id, HttpMethod.GET, makeAuthEntity(), Transfer.class);
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//    }
//        return transfer;
//    }

    public void getAll() {
    Transfer[] transferList = new Transfer[0];
    String type;
    String name;
        try
        {
            System.out.println("-------------------------------------------\n" +
                    "Transfers\n" +
                    "ID          From/To                 Amount\n" +
                    "-------------------------------------------");
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transferList = response.getBody();
            if (transferList != null && transferList.length > 0)
            {
                for (Transfer t : transferList)
                {
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
                    System.out.println(t.getId() + "\t" + type + name + "\t" + t.getAmount());
                }
                System.out.println("---------\n" +
                        "Please enter transfer ID to view details (0 to cancel): \"");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if (Integer.parseInt(input) != 0)
                {
                    boolean found = false;
                    for (Transfer t : transferList)
                    {
                        if (Integer.parseInt(input) == t.getId())
                        {
                            System.out.println("--------------------------------------------\n" +
                                    "Transfer Details\n" +
                                    "--------------------------------------------\n" +
                                    t.toString());
                            found = true;
                        }
                    }
                    if (!found)
                    {
                        System.out.println("Please enter a valid ID");
                    }
                }
            }
            else
            {
                System.out.println("You have no transactions.");
            }
        }
        catch (RestClientResponseException | ResourceAccessException e)
        {
            BasicLogger.log(e.getMessage());
        }
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
