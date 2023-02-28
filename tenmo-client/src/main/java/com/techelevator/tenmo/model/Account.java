package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private int accountID;
    private int userID;
    private BigDecimal balance;

    public Account(){}

    public Account(int account_id, int user_id, BigDecimal balance){
        this.accountID = account_id;
        this.userID = user_id;
        this.balance = balance;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String toString(){
        return "Account ID: " + accountID + "\tUser ID: " + userID + "\tBalance: " + balance;
    }
}
