package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;


@Service
public class TransferSqlDAO implements TransferDAO {
	
	private JdbcTemplate jdbcTemplate;
	private AccountDAO accountDAO;
	private UserDAO userDAO;
		

	
	public TransferSqlDAO(JdbcTemplate jdbcTemplate, AccountDAO accountDAO, UserDAO userDAO) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountDAO = accountDAO;
		this.userDAO = userDAO;
	}
	//creates a new transfer id in the transfers table and fills values in.
	@Override
	public Transfer initiateTransfer(Transfer transfer) {
		Account fromAccount = accountDAO.findUserAccount(transfer.getUserFrom().getId());
		Account toAccount = accountDAO.findUserAccount(transfer.getUserTo().getId());
		String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " + 
				"VALUES (2, 2, ?, ?, ?) RETURNING *; ";
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, fromAccount.getAccountId(), toAccount.getAccountId(), transfer.getAmount());
		if(rs.next()) {
			transfer.setTransferId(rs.getLong("transfer_id"));
		} else {
			transfer = null;
		}
		
		return transfer;
	}
	//During a transfer this updates the database balance, deducting from one and adding the deducted amount to the indicated account
	@Override
	public boolean updateBalances(Transfer transfer) {
		boolean result = false;
		String sql = "BEGIN TRANSACTION; " + 
				"UPDATE accounts " + 
				"SET balance = balance + (SELECT amount FROM transfers WHERE transfer_id = ? ) " + 
				"WHERE account_id = (SELECT account_to FROM transfers WHERE transfer_id = ? ); " + 
				"UPDATE accounts " + 
				"SET balance = balance - (SELECT amount FROM transfers WHERE transfer_id = ? ) " + 
				"WHERE account_id = (SELECT account_from FROM transfers WHERE transfer_id = ? ); "+
				"COMMIT;";
		int updateCount = jdbcTemplate.update(sql, transfer.getTransferId(), transfer.getTransferId(), transfer.getTransferId(), transfer.getTransferId());
		if(updateCount == 3) {
			result = true;
		}
		return result;
	}
	//generates SQL query to locate all transfers for a selected account ID (searchs both account_to and account_from) and puts them into a list
	@Override
	public List<Transfer> findAll() {
		List<Transfer> result = new ArrayList<>();
		String sql = "SELECT transfer_status_desc, transfer_type_desc, transfers.*, fromAccount.user_id as fromUser, toAccount.user_id as toUser\r\n" + 
				"FROM transfers \r\n" + 
				"Join accounts as fromAccount on fromAccount.account_id = transfers.account_from\r\n" + 
				"Join accounts as toAccount on toAccount.account_id = transfers.account_to "
				+ "JOIN transfer_statuses ts ON ts.transfer_status_id = transfers.transfer_status_id\r\n" + 
				"JOIN transfer_types tt ON tt.transfer_type_id = transfers.transfer_type_id "
				+ "ORDER BY transfers.transfer_id";
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		while(rs.next()) {
			Transfer transfer = mapRowToTransfer(rs);
			result.add(transfer);
		}
		return result;
	}
	 // extracted helper method to help make code look cleaner, but sets items from database to a Transfer object.
	 private Transfer mapRowToTransfer(SqlRowSet rs) {
		 Transfer transfer = new Transfer();
	        transfer.setTransferId(rs.getLong("transfer_id"));
	        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
	        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
	        transfer.setUserFrom(userDAO.getUserById(rs.getLong("fromUser")));
	        transfer.setUserTo(userDAO.getUserById(rs.getLong("toUser")));
	        transfer.setAmount(rs.getBigDecimal("amount"));
	        transfer.setTransferStatusMessage(rs.getString("transfer_status_desc"));
	        transfer.setTransferTypeDescription(rs.getString("transfer_type_desc"));
	        
	        return transfer;
	    }



}
