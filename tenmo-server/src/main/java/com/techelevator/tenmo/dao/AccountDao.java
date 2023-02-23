package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
    BigDecimal getBalance(long userId);

    void updateBalance(long userId, BigDecimal amount);

    Account getAccountByAccountID(long accountId);

    Account getAccountByUserID(long userId);
}