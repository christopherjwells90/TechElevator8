package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private TransferService transferService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL), new TransferService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService, TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		//leverages the account service to check our auth'd user to find their current balance.
		BigDecimal currentBalance = accountService.getBalance(currentUser.getToken());
		System.out.println("Current balance: $" + currentBalance);
	
	}
		
		


	private void viewTransferHistory() {
		
		while(true) {
			List<Transfer> allTransfersList = transferService.listOfTransfers(currentUser.getToken());
			List<Transfer> sentTransfers = new ArrayList<>();
			List<Transfer> receivedTransfers = new ArrayList<>();
			List<Long> transferIds = new ArrayList<>();

			for (Transfer transfer : allTransfersList) {
				// get all transfer Id's to check for null pointer when user selects one
				transferIds.add(transfer.getTransferId());
				// if transfer is from my user id display "to user"
				if (transfer.getUserFrom().getId() == currentUser.getUser().getId()) {
					sentTransfers.add(transfer);
				}
				// if transfer is from not my user id display "from user"
				else if (transfer.getUserTo().getId() == currentUser.getUser().getId()) {
					receivedTransfers.add(transfer);
				}
			}
			System.out.println("-------------------------------------------\r\n" + "Transfers\r\n"
					+ "ID          From/To                 Amount\r\n" + "-------------------------------------------");
			for (Transfer transfer : sentTransfers) {
				System.out.println(transfer.getTransferId() + "\t    " + "To: " + transfer.getUserTo().getUsername()
						+ "\t\t    " + transfer.getAmount());
			}
			for (Transfer transfer : receivedTransfers) {
				System.out.println(transfer.getTransferId() + "\t    " + "From: " + transfer.getUserFrom().getUsername()
						+ "\t\t    " + transfer.getAmount());
			}

			System.out.println("\n");
			Long transferInt = console.getUserInputLong("Please enter transfer ID to view details (0 to cancel)");
			if(transferInt == 0) {
				break;
			}
			
			if (transferIds.contains(transferInt)) {
				Long transferId = (long) transferInt;
				Transfer transferById = transferService.findTransferById(transferId, currentUser.getToken());

				System.out.println("\n----------------\r\n" + "Transfer Details\r\n"
						+ "----------------");
				System.out.println("Id: " + transferById.getTransferId());
				System.out.println("From: " + transferById.getUserFrom().getUsername());
				System.out.println("To: " + transferById.getUserTo().getUsername());
				System.out.println("Type: " + transferById.getTransferTypeDescription());
				System.out.println("Status: " + transferById.getTransferStatusMessage());
				System.out.println("Amount: " + transferById.getAmount());
				System.out.println("----------------");
				break;
			} else {
				System.out.println("\n*Not a valid transaction Id*");
			}
		}
		

	}

	private void viewPendingRequests() {
		// see above, filter also by pending
		
	}

	private void sendBucks() {
		//references transfer service to use method list of users to pull all users other than current user
		List<User> userList = transferService.listOfUsers(currentUser.getToken()); 
		List<Integer> userIdList = new ArrayList<>();

		System.out.println("-------------------------");
		System.out.format("%-15s %-30s", "User ID", "Username");
		System.out.println("\n-------------------------");
		for (User user : userList) {

			System.out.println(user.getId() + "\t\t" + user.getUsername());
			userIdList.add(user.getId());
		}
		System.out.println("-------------------------");
        //Gather input from console ap for the username ID to initiate transfer by searching through all Users for that ID
		int findThisUserId = console.getUserInputInteger("\nPlease select a user you would like to transfer to");
		if (userIdList.contains(findThisUserId)) {
			User sendToUser = null;
			for (User user : userList) {
				if (user.getId() == findThisUserId) {
					sendToUser = user;
				}
			}

			User userTo = sendToUser;

			User userFrom = currentUser.getUser();
			
			//Check to see if the amount to send is available to send, that the money is formated correctly.
			
			BigDecimal amountToSend = console.getUserInputBigDec("\nPlease select amount you would like to transfer");
			BigDecimal amountToSendRound = amountToSend.setScale(2, RoundingMode.DOWN); //Make sure no one sends ha'penny or smaller denominations of money
			BigDecimal currentBalanceOfSender = accountService.getBalance(currentUser.getToken());
			if (amountToSendRound.compareTo(currentBalanceOfSender) == -1
					|| amountToSendRound.compareTo(currentBalanceOfSender) == 0) {
				if (amountToSendRound.compareTo(BigDecimal.ZERO) > 0) {
					transferService.postNewTransfer(userFrom, userTo, amountToSendRound, currentUser.getToken());
					
					System.out.println("\n\t* $" + amountToSendRound + " sent to " + sendToUser.getUsername() + " *");
					
				} else {
					System.out.println("\nYou must send a positive amount.");
				}
			} else {
				System.out.println("\nYou do not have enough TE bucks in your account.");
			}

		} else {
			System.out.println("\nCould not find user ID.");
		}

	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
