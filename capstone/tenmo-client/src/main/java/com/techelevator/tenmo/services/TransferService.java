package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.http.server.reactive.HttpHeadResponseDecorator;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class TransferService {

public static final String API_BASE_URL = "http://localhost:8080/transfers";

private RestTemplate restTemplate = new RestTemplate();
private final Scanner scanner = new Scanner(System.in);
private final UserService userService = new UserService();

private final AccountService accountService = new AccountService();

private AuthenticatedUser user;

public void setAuthenticatedUser(AuthenticatedUser user) {
    this.user = user;
    userService.setAuthenticatedUser(user);
    accountService.setAuthToken(user.getToken());
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
    Transfer[] transferList;
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
            int currentAccountId = accountService.getAccount().getId();
            if (transferList != null && transferList.length > 0)
            {
                for (Transfer t : transferList)
                {

                    if (currentAccountId != t.getFromAccount())
                    {
                        type = "To :";
                        name = String.valueOf(t.getToAccount());
                    }
                    else
                    {
                        type = "From: ";
                        name = String.valueOf(t.getToAccount());
                    }
                    System.out.println(t.getId() + "\t" + type + name + "\t" + t.getAmount());
                }
                System.out.println("---------\n" +
                        "Please enter transfer ID to view details (0 to cancel): \"");
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

    public void getPending(){
        Transfer[] transferList;
        String type;
        String name;
        try
        {
            System.out.println("-------------------------------------------\n" +
                    "Pending Transfers\n" +
                    "ID          From/To                 Amount\n" +
                    "-------------------------------------------");
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "pending", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transferList = response.getBody();
            if (transferList != null && transferList.length > 0)
            {
                for (Transfer t : transferList)
                {
                    if (user.getUser().getId() != t.getId())
                    {
                        type = "To :";
                        name = String.valueOf(t.getToAccount());
                    }
                    else
                    {
                        type = "From: ";
                        name = String.valueOf(t.getToAccount());
                    }
                    System.out.println(t.getId() + "\t" + type + name + "\t" + t.getAmount());
                }
                System.out.println("---------\n" +
                        "Please enter transfer ID approve or reject (0 to cancel): \"");
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

                            System.out.println("---------\n" +
                                    "Press 1 to approve or press 2 to reject (0 to cancel): \"");
                            String approveOrReject = scanner.nextLine();
                            if(Integer.parseInt(approveOrReject) == 1){

                                t.setStatusId(2);
                                System.out.println("Approved :)");
                            } else if (Integer.parseInt(approveOrReject) == 2){
                                t.setStatusId(3);
                                System.out.println("Rejected :(");
                            }
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
                System.out.println("You have no pending transactions.");
            }
        }
        catch (RestClientResponseException | ResourceAccessException e)
        {
            BasicLogger.log(e.getMessage());
        }
    }

    public void sendBucks() {
        TransferDto transfer = new TransferDto();
        userService.getAllAccounts();
        System.out.println("---------\n" +
                "\n" +
                "Enter ID of user you are sending to (0 to cancel):\n");
        String sendTo = scanner.nextLine();
        System.out.println("Enter amount:");
        String amount = scanner.nextLine();
        // check if valid string(BIG DEC)
        // try { new BigDecemal(amount) }
        transfer.setUserId(Integer.parseInt(sendTo));
        transfer.setAmount(amount);
        transfer.setTypeId(2);

        HttpEntity<TransferDto> entity = makeTransferEntity(transfer);

        Transfer newTransfer = new Transfer();

        try {
            newTransfer  = restTemplate.postForObject(API_BASE_URL, entity, Transfer.class);
            if (newTransfer != null) {
                System.out.println(newTransfer.toString());
            }
            else {
                System.out.println("Your transfer was not successful.");
            }

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }



    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<TransferDto> makeTransferEntity(TransferDto transfer){
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(user.getToken());
    return new HttpEntity<>(transfer, headers);
    }

}
