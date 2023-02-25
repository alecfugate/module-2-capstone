package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

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
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }


    public Double getUserInputDouble(String prompt) {
        Double result = null;
        do {
            System.out.print(prompt+": ");
            String userInput = scanner.nextLine();
            try {
                result = Double.parseDouble(userInput);
                BigDecimal bd = BigDecimal.valueOf(result);
                if (bd.scale() < 3 && bd.compareTo(BigDecimal.ZERO) > 0) {
                    continue;
                } else {
                    System.out.println("\n*** " + userInput + " has too many decimal places or is negative ***\n");
                    result = null;
                }
            } catch(NumberFormatException e) {
                System.out.println("\n*** " + userInput + " is not valid ***\n");
            }
        } while(result == null);
        return result;
    }

    public Integer getUserInputInteger(String prompt) {
        Integer result = null;
        do {
            System.out.print(prompt+": ");
            String userInput = scanner.nextLine();
            try {
                result = Integer.parseInt(userInput);
            } catch(NumberFormatException e) {
                System.out.println("\n" + "*** " + userInput + " is not valid ***" + "\n");
            }
        } while(result == null);
        return result;
    }

    public String getUserInput(String prompt) {
        System.out.print(prompt+": ");
        return scanner.nextLine();
    }


    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printUsers(User[] users, String userName) {
        // list all user except current user
        for(User user: users) {
            if (user.getUsername().equals(userName)) continue;
            System.out.println(user.getId() + "          " + user.getUsername());
        }
        System.out.println("-------------------------------");
    }

    public void printTransferDetails(int id, String fromUsername, String toUsername, String transferTypeDesc, String transferStatusDesc, BigDecimal amount) {
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        System.out.println("Id: " + id);
        System.out.println("From: " + fromUsername);
        System.out.println("To: " + toUsername);
        System.out.println("Type: " + (transferTypeDesc != null ? transferTypeDesc : "Unknown"));
        System.out.println("Status: " + transferStatusDesc);
        System.out.println("Amount: $" + amount);
        System.out.println("--------------------------------------------");
    }

//    public void printTransferDetails(int id, String fromUsername, String toUsername, boolean isApproved, String transferStatusDesc, BigDecimal amount) {
//        String transferTypeDesc = isApproved ? "Approved" : "Denied";
//        System.out.println("--------------------------------------------");
//        System.out.println("Transfer Details");
//        System.out.println("--------------------------------------------");
//        System.out.println("Id: " + id);
//        System.out.println("From: " + fromUsername);
//        System.out.println("To: " + toUsername);
//        System.out.println("Type: " + transferTypeDesc);
//        System.out.println("Status: " + transferStatusDesc);
//        System.out.println("Amount: $" + amount);
//        System.out.println("--------------------------------------------");
//    }


    public void printApproveOrRejectOptions() {
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject\n");
    }

}
