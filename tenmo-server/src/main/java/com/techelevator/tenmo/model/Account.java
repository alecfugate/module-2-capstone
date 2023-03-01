package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;


public class Account {

    private int accountId;
    @NotBlank(message = "Must have a user ID associated with the account")
    @Min(message = "User IDs begin at 1001", value = 1001)
    private int userId;
    @NotBlank(message = "Balance cannot be blank or empty")
    private BigDecimal balance;

    public Account(){}
    public Account(int account_id, int user_id, BigDecimal balance){
        this.accountId = account_id;
        this.userId = user_id;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int user_id) {
        this.userId = user_id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String toString(){
        return "Account ID: " + accountId + "\tUser ID: " + userId + "\tBalance: " + balance;
    }
}
