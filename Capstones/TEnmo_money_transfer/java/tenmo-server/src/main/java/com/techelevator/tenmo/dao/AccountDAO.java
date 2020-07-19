package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {
	
	Account findUserAccount(Long user_id);


}
