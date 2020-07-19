package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	
	Transfer initiateTransfer(Transfer transfer);
	List<Transfer> findAll();
	boolean updateBalances(Transfer transfer);

}
