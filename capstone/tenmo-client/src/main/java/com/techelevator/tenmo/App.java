package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class App {
    private final String API_BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();
    private final AccountService accountService = new AccountService(API_BASE_URL, restTemplate);
    private final ConsoleService consoleService = new ConsoleService(accountService);
    private final UserService userService = new UserService(API_BASE_URL, restTemplate);
    private final TransferService transferService = new TransferService(API_BASE_URL, restTemplate);
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
                consoleService.printDangerLn("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        consoleService.printWarningLn("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            consoleService.printSuccessLn("Registration successful. You can now login.");
            return;
        }
        consoleService.printErrorMessage();
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
            return;
        }
        accountService.setAuthenticatedUser(currentUser);
        transferService.setAuthenticatedUser(currentUser);
        userService.setAuthenticatedUser(currentUser);

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
                consoleService.printDangerLn("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        BigDecimal balance;
        balance = accountService.getBalance();
        consoleService.printSuccessLn("Your current account balance is: $" + balance);
    }

    private int viewTransferById() {
        int selection = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        Transfer transfer = transferService.getById(selection);
        if (transfer == null) {
            consoleService.printDangerLn(String.format("Transfer id: %d is not found%n", selection));
            return -1;
        }
        consoleService.printTransfer(transfer);
        return selection;
    }

    private void viewTransferHistory() {
        List<Transfer> transfers = transferService.getAll();
        consoleService.printTransfers(transfers);
        if (!transfers.isEmpty()) {
            viewTransferById();
        }
        mainMenu();
    }

    private void viewPendingRequests() {
        List<Transfer> transfers = transferService.getPending();
        consoleService.printTransfers(transfers);
        if (!transfers.isEmpty()) {
            int transferId = viewTransferById();
            int selection = consoleService.promptForInt("Press 1 to approve or press 2 to reject (0 to cancel): ");
            if (selection == 1) {
                if (transferService.approveTransfer(transferId) == null) {
                    consoleService.printDangerLn("Oops, something went wrong, transfer can't be approved.");
                } else {
                    consoleService.printSuccessLn("Approved :)");
                }
            } else if (selection == 2) {
                if (transferService.declineTransfer(transferId) == null) {
                    consoleService.printDangerLn("Oops, something went wrong, transfer can't be approved.");
                } else {
                    consoleService.printDangerLn("Rejected :(");
                }
            }
        }
        mainMenu();
    }

    private void sendBucks() {
        consoleService.printUsers(userService.getAllUsers());
        int userId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");
        Transfer transfer = transferService.sendBucks(userId, amount);
        if (transfer == null) {
            consoleService.printDangerLn("Your request was not successful.");
            return;
        }
        consoleService.printTransfer(transfer);
    }

    private void requestBucks() {
        consoleService.printUsers(userService.getAllUsers());
        int userId = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel): ");
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");
        Transfer transfer = transferService.requestBucks(userId, amount);
        if (transfer == null) {
            consoleService.printDangerLn("Your request was not successful.");
            return;
        }
        consoleService.printTransfer(transfer);
    }
}
