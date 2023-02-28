package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.techelevator.tenmo.exceptions.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;
    Logger logger = LoggerFactory.getLogger(JdbcAccountDao.class);

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    @Override
//    public BigDecimal getBalance(long userId) {
//        String sqlFindBalanceById= "SELECT balance FROM account WHERE user_id = ?";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindBalanceById,userId);
//        BigDecimal balance = new BigDecimal(0.00);
//        while (results.next()){
//            balance = results.getBigDecimal("balance");
//        }
//
//        return balance;
//    }

    @Override
    public BigDecimal getBalanceByAccountID(long accountId) {
        try {
            String sql = "SELECT balance FROM account WHERE account_id = ?";
            return jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public Account[] getBalance(long userID) {
        List<Account> decimalList = new ArrayList<>();

        try {
            String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userID);
            while (result.next()) {
                Account account = mapRowToAccount(result);
                decimalList.add(account);
            }
            Account[] accounts = new Account[decimalList.size()];
            decimalList.toArray(accounts);

            if(accounts.length > 0) {
                logger.info("getBalance called by: " + userID);
                logger.info("returned account array of size: " + accounts.length);
                logger.info("account at element[0]: " + accounts[0].toString());
            }

            return accounts;
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }


    @Override
    public void updateBalance(long userId, BigDecimal amount) {
        String sql = "UPDATE account SET balance = balance + ? WHERE user_id = ?";
        jdbcTemplate.update(sql, amount, userId);
    }

    @Override
    public Account getAccountByAccountID(long accountId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            return null;
        }
    }

    @Override
    public Account[] getAccountsByUserID(long userId) {
        List<Account> accountList = new ArrayList<>();


        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            accountList.add(mapRowToAccount(results));
        }
        Account[] accountArray = new Account[accountList.size()];
        accountList.toArray(accountArray);
        return accountArray;
    }

    @Override
    @Transactional
    public void updateBalanceTransfer(BigDecimal amount, int accountIdFrom, int accountIdTo) throws  InsufficientFundsException{
        String sql = "UPDATE account " +
                "SET balance = balance - ? " + // new updated sender balance
                "WHERE account_id = ?; ";
        jdbcTemplate.update(sql, amount, accountIdFrom);

        sql = "UPDATE account " +
                "SET balance = balance + ? " + // new updated sender balance
                "WHERE account_id = ?; ";
        jdbcTemplate.update(sql, amount, accountIdTo);
    }

    public boolean checkBalance(BigDecimal amount, int accountIdFrom) throws InsufficientFundsException {
        String sql = "SELECT (balance >= ?) as is_valid " +
                "FROM account " +
                "WHERE account_id = ?;";
        boolean isValid = jdbcTemplate.queryForObject(sql, boolean.class, amount, accountIdFrom);
            if (isValid)
                return true;
            else {
                throw new InsufficientFundsException();
            }
    }

    //
    // New stuff
    //
    /* May not be necessary using variable constraints on Transfer.class */
    public boolean checkAmount(BigDecimal amount) throws InsufficientFundsException{
        //Checks the signum of the given amount, if it's negative it will throw an exception
        // signum will check the inverse state of number so +/0/- or literally 1/0/-1
        if(amount.signum() == -1 || amount.signum() == 0){
            throw new InsufficientFundsException();
        } else {
            return true;
        }
    }


    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUser_id(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}