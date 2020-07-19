package com.techelevator.tenmo.controller;


import java.math.BigDecimal;
import java.security.Principal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Account;


@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {
	 private AccountDAO accountDAO;
	 private UserDAO userDAO;
	 
	 
	 public AccountController (AccountDAO accountDAO, UserDAO userDAO) {
		 this.accountDAO = accountDAO;
		 this.userDAO = userDAO;
	 }
	 //implement Account and user DAO's to retrieve corresponding data specifically on the balance in the account table
	 @RequestMapping(value = "/balance", method = RequestMethod.GET)
	    public BigDecimal balance(Principal principal) {
		 Long currentUser = userDAO.findIdByUsername(principal.getName());
		 BigDecimal currentBalance = accountDAO.findUserAccount(currentUser).getBalance();
		 return currentBalance;
	      
	    }
	 

	 

}
