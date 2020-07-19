package com.techelevator;

import java.math.BigDecimal;

public class Nickel implements Coin{
	
	@Override
	public String getName() {
		return "Nickel";
	}
	
	@Override
	public BigDecimal getValue() {
		return new BigDecimal("5");
	}

}
