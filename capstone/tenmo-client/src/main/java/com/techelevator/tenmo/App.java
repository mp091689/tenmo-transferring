package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;

public class App {
    private final String API_BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();
    private final AccountService accountService = new AccountService(API_BASE_URL, restTemplate);
    private final ConsoleService consoleService = new ConsoleService(accountService);
    private final UserService userService = new UserService(API_BASE_URL, restTemplate);
    private final TransferService transferService = new TransferService(API_BASE_URL, restTemplate, userService);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL, restTemplate);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
//        UserCredentials credentials = new UserCredentials("1", "1");
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            accountService.setAuthenticatedUser(currentUser);
            transferService.setAuthenticatedUser(currentUser);
            userService.setAuthenticatedUser(currentUser);
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        BigDecimal balance;
        balance = accountService.getBalance();
        System.out.println("Your current account balance is: $" + balance);
    }

    private void viewTransferHistory() {
        Transfer[] transfers = transferService.getAll();
        consoleService.printTransfers(transfers);
        Transfer transfer = null;
        int selection = -1;
        while (transfer == null && selection != 0) {
            selection = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
            int finalSelection = selection;
            transfer = Arrays.stream(transfers).filter(t -> t.getId() == finalSelection).findFirst().orElse(null);
            if (transfer == null) {
                System.out.printf("Transfer id: %d is not found%n", selection);
                continue;
            }
            consoleService.printTransfer(transfer);
        }
        mainMenu();
    }

    private void viewPendingRequests() {
        transferService.getPending();
    }

    private void sendBucks() {
        transferService.sendBucks();
    }

    private void requestBucks() {
        transferService.requestBucks();
    }

}
