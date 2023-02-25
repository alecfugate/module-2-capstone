package com.techelevator.tenmo;

import com.techelevator.tenmo.exceptions.InvalidTransferIdChoiceException;
import com.techelevator.tenmo.exceptions.InvalidUserChoiceException;
import com.techelevator.tenmo.exceptions.UserNotFoundException;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;



public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    private AccountService accountService = new AccountService(API_BASE_URL);

    private TransferService transferService = new TransferService(API_BASE_URL);

    private UserService userService = new UserService(API_BASE_URL);



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
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
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
		// TODO Auto-generated method stub
        BigDecimal balance = accountService.getBalance(currentUser);
        System.out.println("Your current account balance is: $" + balance);
    }



    private void viewTransferHistory() {
        Transfer[] transfers = transferService.getTransfersFromUserId(currentUser);
        if(transfers ==null) {
            consoleService.getUserInput("\nYou don't have any transfers, press Enter to continue");
            return;
        }

        System.out.println("--------------------------------------------");
        System.out.println("Transfer History");
        System.out.println("ID             From/To               Amount");
        System.out.println("--------------------------------------------");

        for(Transfer transfer: transfers) {
            printTransferForUser(currentUser, transfer);
        }

        int transferIdChoice = consoleService.getUserInputInteger("\nPlease enter transfer ID to view details (0 to cancel)");
        if (transferIdChoice > 0) {
            Transfer transferChoice = validateTransferIdChoice(transferIdChoice, transfers, currentUser);
            if (transferChoice != null) {
                    consoleService.printTransferDetails(
                            transferChoice.getTransferId(),
                            transferChoice.getUserFrom(),
                            transferChoice.getUserTo(),
                            transferChoice.getTransferTypeDesc(),
                            transferChoice.getTransferStatusDesc(),
                            transferChoice.getAmount());
            } else {
                System.out.println("Invalid transfer ID");
            }
        }
    }

    private void viewPendingRequests() {
        Transfer[] transfers = transferService.getPendingTransfersByUserId(currentUser);
        if (transfers == null) {
            consoleService.getUserInput("\nYou don't have any pending requests, press Enter to continue");
            return;
        }

        System.out.println("-------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID          To                     Amount");
        System.out.println("-------------------------------------------");
        for (Transfer transfer : transfers) {
            String fromOrTo;
            String toUsername = transfer.getUserTo();
            String amount = String.format("$ %.2f", transfer.getAmount());
            if (toUsername.equals(currentUser.getUser().getUsername())) {
                fromOrTo = "From: " + transfer.getUserFrom();
                toUsername = transfer.getUserTo();
            } else {
                fromOrTo = "To: " + transfer.getUserTo();
                toUsername = "Me";
            }
            System.out.printf("%-11d%-21s%-10s\n", transfer.getTransferId(), toUsername, amount);
        }
        System.out.println("-------------------------------------------");

        // Ask to choose pending transfer ID
        int transferIdChoice = consoleService.getUserInputInteger("\nPlease enter transfer ID to approve/reject (0 to cancel)");
        Transfer transferChoice = validateTransferIdChoice(transferIdChoice, transfers, currentUser);
        if (transferChoice != null) {
            approveOrReject(transferChoice, currentUser);
        }
    }

	private void sendBucks() {
		// TODO Auto-generated method stub
        User[] users = userService.getAllUsers(currentUser);
        printUserOptions(currentUser, users);

        int userIdChoice = consoleService.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
        if (validateUserChoice(userIdChoice, users, currentUser)) {
            BigDecimal amountChoice = new BigDecimal(consoleService.getUserInputDouble("Enter amount"));
            createTransfer(userIdChoice, amountChoice, 2, 2);

        }
	}

    private void requestBucks () {
        // display list of user except current user
        User[] users = userService.getAllUsers(currentUser);
        printUserOptions(currentUser, users);
        int userIdChoice = consoleService.getUserInputInteger("Enter ID of user you are requesting from (0 to cancel)");
        if (validateUserChoice(userIdChoice, users, currentUser)) {
            BigDecimal amountChoice = new BigDecimal(consoleService.getUserInputDouble("Enter amount"));
            createTransfer(userIdChoice, amountChoice, 1, 1 );

        }
    }


    private void printTransferForUser(AuthenticatedUser authenticatedUser, Transfer transfer) {
        String fromOrTo;
        String username;
        if (transfer.getAccountFrom() == authenticatedUser.getUser().getId()) {
            fromOrTo = "From: ";
            username = transfer.getUserTo();
        } else {
            fromOrTo = "To: ";
            username = transfer.getUserFrom();
        }
        String amount = String.format("$%.2f", transfer.getAmount());
        System.out.printf("%-10d%-24s%-10s\n", transfer.getTransferId(), fromOrTo + username, amount);
    }


    private void printUserOptions(AuthenticatedUser currentUser, User[] users) {

        System.out.println("-------------------------------");
        System.out.println("Users");
        System.out.println("ID            Name");
        System.out.println("-------------------------------");

        // list of user, not display current user
        consoleService.printUsers(users, currentUser.getUser().getUsername());

    }

    private boolean validateUserChoice(int userIdChoice, User[] users, AuthenticatedUser currentUser) {
        if(userIdChoice != 0) {
            try {
                boolean validUserIdChoice = false;
                if(userIdChoice == currentUser.getUser().getId()) {
                    throw new InvalidUserChoiceException();
                }

                for (User user : users) {
                    if (user.getId() == userIdChoice) {
                        validUserIdChoice = true;
                        break;
                    }
                }
                if (!validUserIdChoice) {
                    throw new UserNotFoundException();
                }
                return true;
            } catch (UserNotFoundException | InvalidUserChoiceException e) {
                consoleService.getUserInput(e.getMessage()+", Press Enter to continue");
            }
        }
        return false;
    }


    private Transfer createTransfer (int accountChoiceUserId, BigDecimal amount, int transferTypeId, int transferStatusId){
        // method to handle sendbucks and request bucks
        Account accountToId;
        Account accountFromId;
        // get Account ID from current user and current choice user
        if(transferTypeId==2) {
            accountToId = accountService.getAccountByUserId(currentUser, accountChoiceUserId);
            accountFromId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId());
        } else {
            accountToId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId());
            accountFromId = accountService.getAccountByUserId(currentUser, accountChoiceUserId);
        }

        Transfer transfer = new Transfer();
        transfer.setAccountFrom(accountFromId.getAccount_id());
        transfer.setAccountTo(accountToId.getAccount_id());
        transfer.setAmount(amount);
        transfer.setTransferStatusId(transferStatusId);
        transfer.setTransferTypeId(transferTypeId);

        String message = transferService.createTransfer(currentUser, transfer);
        consoleService.getUserInput(message+" Press Enter to continue");
        return transfer;
    }


    private void printTransferDetails(AuthenticatedUser currentUser, Transfer transferChoice) {
        int id = transferChoice.getTransferId();
        BigDecimal amount = transferChoice.getAmount();

        String fromUserName = transferChoice.getUserFrom();
        // add Me word if it's current user
        if(isMe(currentUser,fromUserName))
            fromUserName=fromUserName+" (Me)";

        // add Me world if it's current user
        String toUserName = transferChoice.getUserTo();
        if(isMe(currentUser,toUserName))
            toUserName=toUserName+" (Me)";

        consoleService.printTransferDetails(id, fromUserName, toUserName,
                transferChoice.getTransferTypeDesc(), transferChoice.getTransferStatusDesc(), amount);
        consoleService.getUserInput("\nPress Enter to continue");
    }

    private boolean isMe(AuthenticatedUser currentUser, String userName) {
        if(currentUser.getUser().getUsername().equals(userName)) return true;
        else return false;
    }

    private Transfer validateTransferIdChoice(int transferIdChoice, Transfer[] transfers, AuthenticatedUser currentUser) {
        Transfer transferChoice = null;
        if(transferIdChoice != 0) {
            try {
                boolean validTransferIdChoice = false;
                for (Transfer transfer : transfers) {
                    if (transfer.getTransferId() == transferIdChoice) {
                        validTransferIdChoice = true;
                        transferChoice = transfer;
                        break;
                    }
                }
                if (!validTransferIdChoice) {
                    throw new InvalidTransferIdChoiceException();
                }
            } catch (InvalidTransferIdChoiceException e) {
                consoleService.getUserInput(e.getMessage()+", Press Enter to continue");
            }
        }
        return transferChoice;
    }

    private void approveOrReject(Transfer pendingTransfer, AuthenticatedUser authenticatedUser) {
        //method to approve or reject transfer
        consoleService.printApproveOrRejectOptions();
        int choice = consoleService.getUserInputInteger("Please choose an option");

        if(choice != 0) {
            if(choice == 1) {
                pendingTransfer.setTransferStatusId(2);
            } else if (choice == 2) {
                pendingTransfer.setTransferStatusId(3);
            } else {
                consoleService.getUserInput("Invalid choice, Press Enter to continue"); return;
            }
            String message = transferService.updateTransfer(currentUser, pendingTransfer);
            consoleService.getUserInput(message+" Press Enter to continue");
        }

    }


}
