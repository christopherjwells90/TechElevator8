package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;

import com.techelevator.tenmo.dao.UserDAO;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/transfer")
public class TransferController {
		 private AccountDAO accountDAO;
		 private UserDAO userDAO;
		 private TransferDAO transferDAO;
		 
		 
		 public TransferController (AccountDAO accountDAO, UserDAO userDAO, TransferDAO transferDAO) {
			 this.accountDAO = accountDAO;
			 this.userDAO = userDAO;
			 this.transferDAO = transferDAO;
	}
    //access a list of users that we use in transfer method, shows all users except current user
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> showListOfUsers(Principal principal) {
		List<User> users = new ArrayList<>();
		users = userDAO.findAll();
		List<User> tranferableUsers = new ArrayList<>();
		for (User user : users) {
			if (!user.getUsername().equals(principal.getName())) {
				tranferableUsers.add(user);
			}

		}
		return tranferableUsers;
	}
	//gets all transfers that mentions the current user (in either sending or receiving TE bucks)
	@RequestMapping(value = "/transactions", method = RequestMethod.GET)
	public List<Transfer> getTransfers(Principal principal) {
		List<Transfer> transfers = null;
		Long user_id = userDAO.findIdByUsername(principal.getName());
		transfers = transferDAO.findAll();
		List<Transfer> currentUserTransfers = new ArrayList<>();
		for (Transfer transfer : transfers) {
			if (transfer.getUserTo().getId() == user_id || transfer.getUserFrom().getId() == user_id) {
				currentUserTransfers.add(transfer);
			}

		}
		
		return currentUserTransfers;
	}
	
	//locates and generates two account ID's for a transfer, and generates a new transfer ID, the updates both balances
	@RequestMapping( method = RequestMethod.POST)
	public Transfer initiateNewTransfer(@RequestBody Transfer transfer) {
		Transfer newTransfer = null;
		newTransfer = transferDAO.initiateTransfer(transfer);
		transferDAO.updateBalances(newTransfer);
		return newTransfer;
	}
	//references all transfers and looks through the list to find the matching transfer ID
	@RequestMapping(value = "/transactions/{id}", method = RequestMethod.GET)
	public Transfer getTransferFromId(@PathVariable ("id") Long transferId, Principal principal) {
		List<Transfer> transfers = null;
		Transfer transferFromId = new Transfer();
		
		transfers = transferDAO.findAll();
		
		for (Transfer transfer : transfers) {
			if (transfer.getTransferId() == transferId) {
				transferFromId = transfer;
			}

		}
		
		return transferFromId;
	}
	

}
