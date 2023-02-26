package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    BigDecimal getBalanceByAccountID(long accountId);

    List<BigDecimal> getBalance(long userID);

    void updateBalance(long userId, BigDecimal amount);

    Account getAccountByAccountID(long accountId);

    List<Account> getAccountsByUserID(long userId);

    void checkAndUpdateBalance(BigDecimal amount, int accountIdFrom, int accountIdTo) throws InsufficientFundsException;

}