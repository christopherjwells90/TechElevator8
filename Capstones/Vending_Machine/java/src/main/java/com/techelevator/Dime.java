package com.techelevator;

import java.math.BigDecimal;

public class Dime implements Coin {
	
	@Override
	public String getName() {
		return "Dime";
	}
	
	@Override
	public BigDecimal getValue() {
		return new BigDecimal("10");
	}
}
