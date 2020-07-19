package com.techelevator;

import java.math.BigDecimal;

public class VendingMachineItem {
	private String location;
	private String name;
	private BigDecimal price;
	private String type;
	private String message;
	
	public VendingMachineItem(String location, String name, String price, String type) {
		this.location = location;
		this.name = name;
		this.price = new BigDecimal(price);
		this.type = type;
		if (this.type.equals("Chip")) {
			message = "Crunch Crunch, Yum!";
		}
		if (this.type.equals("Candy")) {
			message = "Munch Munch, Yum!";
		}
		if (this.type.equals("Drink")) {
			message = "Glug Glug, Yum!";
		}
		if (this.type.equals("Gum")) {
			message = "Chew Chew, Yum!";
		}
	}
	
	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public String toString() {
		return getLocation()+" "+getName()+" "+getPrice();
	}
	
}
