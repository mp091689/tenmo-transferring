package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {
    private final Scanner scanner = new Scanner(System.in);
    private final AccountService accountService;
    private final int WIDTH = 40;

    private final String COLOR_RESET = "\u001b[0m";
    private final String COLOR_DANGER = "\u001b[31m";
    private final String COLOR_WARNING = "\u001b[33m";
    private final String COLOR_SUCCESS = "\u001b[32m";

    public ConsoleService(AccountService accountService) {
        this.accountService = accountService;
    }

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        printWarningLn(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        printWarningLn(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                printDangerLn("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        printWarningLn(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                printDangerLn("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        printWarningLn("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        printDangerLn("An error occurred. Check the log for details.");
    }

    public void printTransfers(List<Transfer> transfers) {
        System.out.println("-".repeat(WIDTH));
        System.out.println("Transfers");
        System.out.printf("%s%19s%19s%n", "ID", "From/To", "Amount");
        System.out.println("-".repeat(WIDTH));
        for (Transfer transfer : transfers) {
            int currentAccountId = accountService.getAccount().getId();
            String direction = currentAccountId == transfer.getFromAccount().getId()
                    ? "To: " + transfer.getToAccount().getUser().getUsername()
                    : "From: " + transfer.getFromAccount().getUser().getUsername();

            System.out.printf("%4s%16s%12s%8.2f%n", transfer.getId(), direction, "$\t", transfer.getAmount());
        }
        System.out.println("-".repeat(9));
    }

    public void printTransfer(Transfer transfer) {
        System.out.println("-".repeat(WIDTH));
        System.out.println("Transfer Details");
        System.out.println("-".repeat(WIDTH));
        System.out.printf("%-8s%d%n", "ID:", transfer.getId());
        System.out.printf("%-8s%s%n", "From:", transfer.getFromAccount().getUser().getUsername());
        System.out.printf("%-8s%s%n", "To:", transfer.getToAccount().getUser().getUsername());
        System.out.printf("%-8s%s%n", "Type:", transfer.getType());
        System.out.printf("%-8s%s%n", "Status:", transfer.getStatus());
        System.out.printf("%-8s$%.2f%n", "Amount:", transfer.getAmount());
    }

    public void printUsers(List<User> users) {
        System.out.println("-".repeat(WIDTH));
        System.out.println("Users");
        System.out.printf("%s%19s%n", "ID", "Name");
        System.out.println("-".repeat(WIDTH));
        for (User user : users) {
            System.out.printf("%4s%16s%n", user.getId(), user.getUsername());
        }
        System.out.println("-".repeat(9));
    }

    public void printDangerLn(String msg) {
        System.out.println(COLOR_DANGER + msg + COLOR_RESET);
    }

    public void printWarningLn(String msg) {
        System.out.println(COLOR_WARNING + msg + COLOR_RESET);
    }

    public void printSuccessLn(String msg) {
        System.out.println(COLOR_SUCCESS + msg + COLOR_RESET);
    }
}
