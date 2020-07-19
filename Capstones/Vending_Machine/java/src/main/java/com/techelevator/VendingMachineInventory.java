package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class VendingMachineInventory {
	//String is item location, Integer is inventory count
	private Map<String, Integer> inventory = new HashMap<String, Integer>();
	private Map<String, VendingMachineItem> locationToItem = new HashMap<String, VendingMachineItem>();
	//Maps don't save order, to save order of buttons we are using a Queue
	private Queue<String> locationQueue = new LinkedList<String>();
	File inventoryFile;
	Scanner inputFile;
	private final int STARTING_ITEM_AMOUNT = 5;
	
	
	public VendingMachineInventory() {
		inventoryFile = new File("./VendingMachine.txt");
		try (Scanner inputFile = new Scanner(inventoryFile)) {
			while (inputFile.hasNextLine()) {
				String itemLine = inputFile.nextLine();
				String[] itemArray = itemLine.split("\\|");
				VendingMachineItem currentItem = new VendingMachineItem(itemArray[0], itemArray[1], itemArray[2], itemArray[3]);
				int startingAmount = STARTING_ITEM_AMOUNT;
				//set inventory amount per location
				inventory.put(currentItem.getLocation(), startingAmount);
				//set item per location
				locationToItem.put(currentItem.getLocation(), currentItem);
				//record order locations are entered in
				locationQueue.offer(currentItem.getLocation());
			}
		} catch (Exception e) {
			System.out.println("There was a problem updating the inventory!");
		}
	}
	
	public String purchaseItem(String location) {
		String message;
		if (!inventory.containsKey(location)) {
			message = "This item doesn't exist!";
		}
		if (inventory.get(location).compareTo(0) == 0) {
			message = "Sorry, sold out!";
		} else {
			inventory.put(location, inventory.get(location)-1);
			message = locationToItem.get(location).getMessage();
		}
		return message;
	}

	public String displayList() {
		String displayList ="";
		for (int i = 0; i < locationQueue.size(); i++) {
			// pulls location off front of Queue, should be line 1 of VendingMachine.txt
			String location = locationQueue.poll();
			// use location to get item from locationToItem
			VendingMachineItem currentItem = locationToItem.get(location);
			// print out info from the vending machine item
			if (inventory.get(location).equals(0)) {
				displayList+="SOLD OUT\n";
			} else {
				displayList+=currentItem.toString()+"\n";
			}
			// put location back at end of queue so it works again later
			locationQueue.offer(location);
		}
		return displayList;
	}
	
	public BigDecimal getPriceBasedOnLocation(String location) {
		return (locationToItem.get(location).getPrice());
	}
	
	public String getNameBasedOnLocation(String location) {
		return (locationToItem.get(location).getName());
	}
	
	
	
	public boolean haveItem(String location) {
		if (inventory.get(location) == 0) {
			return false;
		}
		else return true;
	}

}
