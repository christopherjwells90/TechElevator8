package com.techelevator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ChangeReturn {
	
	public static String changeReturn (BigDecimal currentBalance) {
		Coin[] coins = new Coin[] { new Quarter(), new Dime(), new Nickel(), new Penny()
		};
		currentBalance = currentBalance.multiply(new BigDecimal("100"));
		String result = "Your change is ";
		
		Map<Coin, BigDecimal> change = new HashMap<Coin, BigDecimal>();
		
		for (Coin coin: coins){ //iterate through the coins array
			if (currentBalance.compareTo(new BigDecimal("0")) < 0) {break;}
			BigDecimal cnt = currentBalance.divide(coin.getValue(), 0, 1);
			if (cnt.compareTo(new BigDecimal("0")) > 0) {
				currentBalance = currentBalance.remainder(cnt.multiply(coin.getValue()));
				change.put(coin,cnt);
			}
		}
		for (Coin coin : change.keySet()) {
			result += (change.get(coin)+" " + coin.getName() + "(s) ");
		}
		return result;
	}

}
