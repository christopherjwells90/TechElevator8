package com.techelevator.tenmo.dao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;


@Service
public class AccountSqlDAO implements AccountDAO {
    private JdbcTemplate jdbcTemplate;
    
    
    public AccountSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
	}

    //method to help us connect a user identity to their account, as most of the work we are doing is account or transfer related
	@Override
	public Account findUserAccount(Long userId) {
		Account result = null;
		SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT account_id, accounts.user_id, balance " + "FROM accounts "
				+ "INNER JOIN users ON accounts.user_id = users.user_id WHERE users.user_id = ?;", userId);
		if (rs.next()) {
			result = mapRowToAccount(rs);
		}
		return result;
	}
     //method to accomplish mapping, helps look cleaner, usable if we want to create additional DB access methods
	 private Account mapRowToAccount(SqlRowSet rs) {
	        Account account = new Account();
	        account.setUserId(rs.getLong("user_id"));
	        account.setAccountId(rs.getInt("account_id"));
	        account.setBalance(rs.getBigDecimal("balance"));
	        return account;
	    }
	 




}
