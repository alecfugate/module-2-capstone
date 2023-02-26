package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests{

    protected static final Account ACCOUNT_1 = new Account(1, 1, new BigDecimal("1000.00"));
    protected static final Account ACCOUNT_2 = new Account(2, 2, new BigDecimal("2000.00"));

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getBalance_given_invalidId_returns_null(){
        BigDecimal number = sut.getBalance(-1);

        Assert.assertNull(number);
    }

    @Test
    public void getBalance_returns_correct_balance(){
        BigDecimal number = sut.getBalance(1);

        Assert.assertEquals(number, ACCOUNT_1.getBalance());
    }

    @Test
    public void getAccountByUserID_returns_null_given_invalidID(){
        Account test = sut.getAccountByUserID(-1);

        Assert.assertNull(test);
    }

    @Test
    public void getAccountByUserID_returns_account_given_validID(){
        Account test = sut.getAccountByUserID(1);

        Assert.assertEquals(ACCOUNT_1, test);
    }

    @Test//(expected = InsufficientFundsException.class)
    public void checkAndUpdateBalance_given_negative_value_throws_InsufficientFundsException() {
        try {
            // Changed the input value as a negative number to test
            sut.checkAndUpdateBalance(new BigDecimal("-3000.00"), 1, 2);
            //If the expected exception happens, it will skip to the catch
            //otherwise big L
            Assert.fail("Negative number shouldn't be allowed");
        } catch (InsufficientFundsException e) {
            //Assert.assertTrue(true);
        }
        //Assert.fail();
    }

    @Test
    public void checkAndUpdateBalance_correctly_updates_balance_of_both_accounts(){
        try{
            sut.checkAndUpdateBalance(new BigDecimal("100.00"), 1, 2);
        } catch (InsufficientFundsException e){
            System.err.println("Welp. Something went wrong");
        }
        Assert.assertEquals(new BigDecimal("900.00"), ACCOUNT_1.getBalance());
        Assert.assertEquals(new BigDecimal("2100.00"), ACCOUNT_2.getBalance());
    }


}
