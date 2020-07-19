package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
	
	private Long transferId;
	private int transferTypeId;
	private int transferStatusId;
	private User userFrom;
	private User userTo;
	private BigDecimal amount;
	private String transferStatusMessage;
	private String transferTypeDescription;
	
	public Transfer() {}
	
	public Transfer(Long transferId, int transferTypeId, int transferStatusId, User userFrom, User userTo, BigDecimal amount) {
		this.transferId = transferId;
		this.transferTypeId = transferTypeId;
		this.transferStatusId = transferStatusId;
		this.userFrom = userFrom;
		this.userTo = userTo;
		this.amount = amount;
		
	}

	public Long getTransferId() {
		return transferId;
	}

	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}

	public int getTransferTypeId() {
		return transferTypeId;
	}

	public void setTransferTypeId(int transferTypeId) {
		this.transferTypeId = transferTypeId;
	}

	public int getTransferStatusId() {
		return transferStatusId;
	}

	public void setTransferStatusId(int transferStatusId) {
		this.transferStatusId = transferStatusId;
	}

	public User getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(User userFrom) {
		this.userFrom = userFrom;
	}

	public User getUserTo() {
		return userTo;
	}

	public void setUserTo(User userTo) {
		this.userTo = userTo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTransferStatusMessage() {
		return transferStatusMessage;
	}

	public void setTransferStatusMessage(String transferStatusMessage) {
		this.transferStatusMessage = transferStatusMessage;
	}

	public String getTransferTypeDescription() {
		return transferTypeDescription;
	}

	public void setTransferTypeDescription(String transferTypeDescription) {
		this.transferTypeDescription = transferTypeDescription;
	}
	


}
