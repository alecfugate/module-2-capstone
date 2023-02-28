package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests{


    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getBalance_given_invalidID_returns_empty_array(){
        Account[] testList = sut.getBalance(-1);

        Assert.assertEquals(0, testList.length);
    }

    @Test
    public void getBalance_given_validID_returns_correct_list(){
        Account[] testList = sut.getBalance(USER_1.getId());

        Assert.assertEquals(3, testList.length);
        Assert.assertEquals(ACCOUNT_1.getBalance(), testList[0].getBalance());
        Assert.assertEquals(ACCOUNT_3.getBalance(), testList[1].getBalance());
        Assert.assertEquals(ACCOUNT_4.getBalance(), testList[2].getBalance());
    }

    @Test
    public void getBalanceByAccountID_given_invalidId_returns_null(){
        BigDecimal number = sut.getBalanceByAccountID(-1);

        Assert.assertNull(number);
    }

    @Test
    public void getBalanceByAccountID_returns_correct_balance(){
        BigDecimal number = sut.getBalanceByAccountID(ACCOUNT_1.getAccountId());

        Assert.assertEquals(ACCOUNT_1.getBalance(), number);
    }

    @Test
    public void getAccountsByUserID_returns_empty_given_invalidID(){
        Account[] test = sut.getAccountsByUserID(-1);

        Assert.assertEquals(0, test.length);
    }

    @Test
    public void getAccountsByUserID_returns_accounts_given_validID(){
        Account[] test = sut.getAccountsByUserID(USER_1.getId());

        Assert.assertEquals(3, test.length);
        Assert.assertEquals(ACCOUNT_1.getAccountId(), test[0].getAccountId());
        Assert.assertEquals(ACCOUNT_3.getAccountId(), test[1].getAccountId());
        Assert.assertEquals(ACCOUNT_4.getAccountId(), test[2].getAccountId());
    }

    @Test//(expected = InsufficientFundsException.class)
    public void checkBalance_given_negative_value_throws_InsufficientFundsException() {
        boolean pass = false;
        try {
            // Changed the input value as a negative number to test
            sut.checkBalance(new BigDecimal("3000.00"), ACCOUNT_1.getAccountId());
            //If the expected exception happens, it will skip to the catch
            //otherwise big L
        } catch (InsufficientFundsException e) {
            pass = true;
        }
        Assert.assertEquals(true, pass);
    }

    @Test
    public void checkAndUpdateBalance_correctly_updates_balance_of_both_accounts(){
        try{
            sut.updateBalanceTransfer(new BigDecimal(100.00), ACCOUNT_1.getAccountId(), ACCOUNT_2.getAccountId());
        } catch (InsufficientFundsException e){
            System.err.println("Welp. Something went wrong");
        }
        Assert.assertEquals(new BigDecimal("900.00"), sut.getBalanceByAccountID(ACCOUNT_1.getAccountId()));
        Assert.assertEquals(new BigDecimal("2100.00"), sut.getBalanceByAccountID(ACCOUNT_2.getAccountId()));
    }


}
