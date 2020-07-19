package com.techelevator;

import java.math.BigDecimal;

public class Penny implements Coin{
	
	@Override
	public String getName() {
		return "Penny";
	}
	
	@Override
	public BigDecimal getValue() {
		return new BigDecimal("1");
	}

}
