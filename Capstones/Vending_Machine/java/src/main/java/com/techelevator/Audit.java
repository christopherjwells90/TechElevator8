package com.techelevator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Audit {
	
	
	List<String> auditStrings = new ArrayList<String>();
	File auditFile = new File(".", "Log.txt");
	
	public void addFeedMoneyToAudit (BigDecimal addMoney, BigDecimal currentBalance){
		String formattedDate = getDateString();
		String stringToWrite = formattedDate  + " FEED MONEY: $" + addMoney + " $"+ currentBalance+"\n";
		auditStrings.add(stringToWrite);
		writeStringToAudit(stringToWrite, auditFile);
	}
	
	public void addPurchaseToAudit (String itemName, String location, BigDecimal price, BigDecimal currentBalance){
		String formattedDate = getDateString();
		String stringToWrite = formattedDate + " " + itemName + " " + location + " $" + price + " $"+ currentBalance+"\n";
		auditStrings.add(stringToWrite);
		writeStringToAudit(stringToWrite, auditFile);
	}
	
	public void addChangeToAudit (BigDecimal currentBalance){
		String formattedDate = getDateString();
		String stringToWrite = formattedDate  + " GIVE CHANGE: $" + currentBalance + " $0.00"+"\n";
		auditStrings.add(stringToWrite);
		writeStringToAudit(stringToWrite, auditFile);
	}
	
	public void writeStringToAudit(String stringToWrite, File auditFile) {
		try (FileWriter writer = new FileWriter(auditFile, true)) {
			writer.append(stringToWrite);
		} catch (Exception e) {
			System.out.println("Hey bozo I'm writin' here!");
		}
	}
	
	public void createFile() {
		try {
			auditFile.createNewFile();
		} catch (IOException e) {
			
		}
	}
	
	private String getDateString() {
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
		String formattedDate = date.format(formatter);
		return formattedDate;
	}
}
