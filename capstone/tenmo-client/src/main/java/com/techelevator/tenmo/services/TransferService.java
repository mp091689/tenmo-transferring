package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

public class TransferService {

    public final String SERVICE_API_URL;
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    private final RestTemplate restTemplate;
    private AuthenticatedUser user;

    public TransferService(String baseUrl, RestTemplate restTemplate, UserService userService) {
        this.SERVICE_API_URL = baseUrl + "/transfers";
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    public void setAuthenticatedUser(AuthenticatedUser user) {
        this.user = user;
    }

    public Transfer[] getAll() {
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(SERVICE_API_URL, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    public void getPending() {
        Transfer[] transferList;
        String type;
        String name;
        try {
            System.out.println("-------------------------------------------\n" +
                    "Pending Transfers\n" +
                    "ID          From/To                 Amount\n" +
                    "-------------------------------------------");
            ResponseEntity<Transfer[]> response = restTemplate.exchange(SERVICE_API_URL + "/pending", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transferList = response.getBody();
            if (transferList != null && transferList.length > 0) {
                for (Transfer t : transferList) {
                    if (user.getUser().getId() != t.getId()) {
                        type = "To :";
                        name = String.valueOf(t.getToAccount());
                    } else {
                        type = "From: ";
                        name = String.valueOf(t.getToAccount());
                    }
                    System.out.println(t.getId() + "\t" + type + name + "\t" + t.getAmount());
                }
                System.out.println("---------\n" +
                        "Please enter transfer ID approve or reject (0 to cancel): \"");
                String input = scanner.nextLine();
                if (Integer.parseInt(input) != 0) {
                    boolean found = false;
                    for (Transfer t : transferList) {
                        if (Integer.parseInt(input) == t.getId()) {
                            System.out.println("--------------------------------------------\n" +
                                    "Transfer Details\n" +
                                    "--------------------------------------------\n" +
                                    t.toString());
                            found = true;

                            System.out.println("---------\n" +
                                    "Press 1 to approve or press 2 to reject (0 to cancel): \"");
                            String approveOrReject = scanner.nextLine();
                            if (Integer.parseInt(approveOrReject) == 1) {

                                t.setStatusId(2);
                                System.out.println("Approved :)");
                            } else if (Integer.parseInt(approveOrReject) == 2) {
                                t.setStatusId(3);
                                System.out.println("Rejected :(");
                            }
                        }
                    }
                    if (!found) {
                        System.out.println("Please enter a valid ID");
                    }
                }
            } else {
                System.out.println("You have no pending transactions.");
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
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
        if (Integer.parseInt(sendTo) > 0) {
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
                newTransfer = restTemplate.postForObject(SERVICE_API_URL, entity, Transfer.class);
                if (newTransfer != null) {
                    System.out.println(newTransfer.toString());
                } else {
                    System.out.println("Your transfer was not successful.");
                }

            } catch (RestClientResponseException | ResourceAccessException e) {
                BasicLogger.log(e.getMessage());
            }
        } else {
            System.out.println("Returning to main menu.");
        }

    }

    public void requestBucks() {
        TransferDto transfer = new TransferDto();
        userService.getAllAccounts();
        System.out.println("---------\n" +
                "\n" +
                "Enter ID of user you are requesting from (0 to cancel):\n");
        String requestFrom = scanner.nextLine();
        if (Integer.parseInt(requestFrom) > 0) {
            System.out.println("Enter amount:");
            String amount = scanner.nextLine();
            // check if valid string(BIG DEC)
            // try { new BigDecemal(amount) }
            transfer.setUserId(Integer.parseInt(requestFrom));
            transfer.setAmount(amount);
            transfer.setTypeId(1);

            HttpEntity<TransferDto> entity = makeTransferEntity(transfer);

            Transfer newTransfer = new Transfer();

            try {
                newTransfer = restTemplate.postForObject(SERVICE_API_URL, entity, Transfer.class);
                if (newTransfer != null) {
                    System.out.println(newTransfer.toString());
                } else {
                    System.out.println("Your request was not successful.");
                }

            } catch (RestClientResponseException | ResourceAccessException e) {
                BasicLogger.log(e.getMessage());
            }
        } else {
            System.out.println("Returning to main menu.");
        }
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<TransferDto> makeTransferEntity(TransferDto transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transfer, headers);
    }
}
