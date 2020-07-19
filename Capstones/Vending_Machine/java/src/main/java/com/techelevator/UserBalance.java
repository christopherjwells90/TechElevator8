package com.techelevator;

import java.math.BigDecimal;

public class UserBalance {
	private BigDecimal currentBalance;
	
	public UserBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}
	
	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}
	
	public boolean addToCurrentBalance(BigDecimal addMoney) {
		addMoney = addMoney.setScale(2);
		BigDecimal remainder = addMoney.remainder(new BigDecimal("1"));
		if (remainder.compareTo(new BigDecimal("0")) == 0) {
			currentBalance = currentBalance.add(addMoney);
		}
		return (remainder.compareTo(new BigDecimal("0")) == 0);
		
	}
	
	public boolean subtractFromCurrentBalance(BigDecimal subtractMoney) {
		if(subtractMoney.compareTo(currentBalance) <= 0) {
			currentBalance = currentBalance.subtract(subtractMoney);
			return true;
		}
		else return false;
	}
	
	public void zeroBalance() {
		currentBalance = new BigDecimal("0");
	}
	
}
