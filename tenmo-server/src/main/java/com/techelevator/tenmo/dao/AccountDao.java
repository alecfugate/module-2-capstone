package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    BigDecimal getBalanceByAccountID(long accountId);

    Account[] getBalance(long userID);

    void updateBalance(long userId, BigDecimal amount);

    Account getAccountByAccountID(long accountId);

    Account[] getAccountsByUserID(long userId);

    void updateBalanceTransfer(BigDecimal amount, int accountIdFrom, int accountIdTo) throws InsufficientFundsException;

    boolean checkBalance(BigDecimal amount, int accountIdFrom) throws InsufficientFundsException;
}