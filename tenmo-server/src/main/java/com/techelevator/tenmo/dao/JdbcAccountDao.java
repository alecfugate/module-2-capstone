package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.techelevator.tenmo.exceptions.InsufficientFundsException;

import java.math.BigDecimal;

@Repository
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

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
    public BigDecimal getBalance(long userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
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
    public Account getAccountByUserID(long userId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void checkAndUpdateBalance(BigDecimal amount, int accountIdFrom, int accountIdTo) throws  InsufficientFundsException{
        checkBalance(amount, accountIdFrom);
        String sql = "UPDATE account " +
                "SET balance = balance - ? " + // new updated sender balance
                "WHERE account_id = ?; ";
        jdbcTemplate.update(sql, amount, accountIdFrom);

        sql = "UPDATE account " +
                "SET balance = balance + ? " + // new updated sender balance
                "WHERE account_id = ?; ";
        jdbcTemplate.update(sql, amount, accountIdTo);
    }

    private void checkBalance(BigDecimal amount, int accountIdFrom) throws InsufficientFundsException {
        String sql = "SELECT (balance >= ?) as is_valid " +
                "FROM account " +
                "WHERE account_id = ?;";
        SqlRowSet isValid = jdbcTemplate.queryForRowSet(sql, amount , accountIdFrom);
        if (isValid.next()) {
            if (!isValid.getBoolean("is_valid"))
                throw new InsufficientFundsException();
        }

    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccount_id(rs.getInt("account_id"));
        account.setUser_id(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}