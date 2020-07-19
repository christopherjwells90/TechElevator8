package com.techelevator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import com.techelevator.view.Menu;

//classes needed: VendingMachineItem
//Should have Location, Name, Price, Type, and Message Variables

//need Inventory/Storage Class
//Map containing Location of Item with Count of Item
//If count of item is zero, displays "Sold Out"

//need Customer Purchase Class
//tracks balance
//calculates Change

//need Audit class
//writes to a document
//tracks time, date, money entered, balance for money fed in
//tracks time, date, item name, item slot, item price, balance for  item purchase
//tracks time, date , amount of change, remaining balance for transaction finish
public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String PURCHASE_MENU_OPTION_ADD_MONEY = "Add Money To Balance";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT };
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_ADD_MONEY, MAIN_MENU_OPTION_PURCHASE,
			PURCHASE_MENU_OPTION_FINISH_TRANSACTION };

	private Menu menu;
	private VendingMachineInventory vsi = new VendingMachineInventory();
	private Audit audit = new Audit();
	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {

		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				displayItems();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
//				BigDecimal currentBalance = new BigDecimal("20.00");
//				String change = ChangeReturn.changeReturn(currentBalance);
//				System.out.println(change);
//				Testing change return function

				// Initialize balance on purchase choice because user getting getting change
				// upon exit
				UserBalance currentBalance = new UserBalance(new BigDecimal("0"));
				boolean purchaseMenu = true;
				while (purchaseMenu) {
					String purchaseChoice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
					if (purchaseChoice.equals(PURCHASE_MENU_OPTION_ADD_MONEY)) {
						// add money and return balance
						// use while loop to handle exceptions
						getUserInputAndAddMoney(currentBalance);
						
						System.out.println("Current balance is: "+currentBalance.getCurrentBalance()+" dollars");
					} else if (purchaseChoice.equals(MAIN_MENU_OPTION_PURCHASE)) {
						purchaseMenu(currentBalance);
					} else if (purchaseChoice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)) {
						// return change (no dollars, that's what you get for not having small bills)
						audit.addChangeToAudit(currentBalance.getCurrentBalance());
						System.out.println(ChangeReturn.changeReturn(currentBalance.getCurrentBalance()));
						// zero out balance
						currentBalance.zeroBalance();
						// exit purchase menu
						purchaseMenu = false;
					}
				}
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.exit(0);
			}
		}
	}

	private void purchaseMenu(UserBalance currentBalance) {
		// display items
		displayItems();
		// accept purchase input
		String exitMessage = "";
		while (exitMessage.equals("This item doesn't exist!") || exitMessage.equals(""))
		try {
			Scanner userInput = new Scanner(System.in);
			System.out.print("Which entry would you like? Enter an alphanumeric code (ex. A1)>>> ");
			String locationChoice = userInput.nextLine();
			locationChoice = locationChoice.toUpperCase();
			//check if they have enough money
			BigDecimal price = vsi.getPriceBasedOnLocation(locationChoice);
			if (vsi.haveItem(locationChoice)) {
				if (currentBalance.subtractFromCurrentBalance(price)) {
					//ticks item count down by 1
					exitMessage = vsi.purchaseItem(locationChoice);
					String itemName = vsi.getNameBasedOnLocation(locationChoice);
					audit.addPurchaseToAudit(itemName, locationChoice, price, currentBalance.getCurrentBalance());
					System.out.println(exitMessage);
				} else {
					exitMessage = "Sorry, not enough money. Please add money.";
					System.out.println(exitMessage);
				}
			} else {
				exitMessage = "Sorry, sold out!";
				System.out.println(exitMessage);
			}
			
			
		} catch (Exception e) {
			System.out.println("This item doesn't exist!");
		}
		
		// return to purchase menu
	}

	public void getUserInputAndAddMoney(UserBalance currentBalance) {
		boolean validChoice = false;
		while (!validChoice) {
			try  {
				Scanner userInput = new Scanner(System.in);
				System.out.print("Please enter a whole dollar amount to add to your balance>>> ");
				String moneyToAdd = userInput.nextLine();
				BigDecimal amountToAdd = new BigDecimal(moneyToAdd);
				amountToAdd = amountToAdd.setScale(2);
				validChoice = currentBalance.addToCurrentBalance(amountToAdd);
				if (validChoice) {
					audit.addFeedMoneyToAudit(amountToAdd, currentBalance.getCurrentBalance());
				}
			} catch (Exception e) {
				//nothing returns here, non-valid inputs won't update validChoice
			}
			if (validChoice == false) {
				System.out.println("\nThat entry didn't work, please enter a dollar amount with no change\n");
			}
		}
	}

	public void displayItems() {
		System.out.println(vsi.displayList());
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		VendingMachineInventory vsi = new VendingMachineInventory();
		Audit audit = new Audit();
		audit.createFile();
		cli.run();
	}

	public void purchaseItem() {

	}
}
