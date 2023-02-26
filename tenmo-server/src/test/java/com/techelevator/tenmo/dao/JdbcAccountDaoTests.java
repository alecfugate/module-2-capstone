package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests{


    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getBalance_given_invalidID_returns_empty_list(){
        List<BigDecimal> testList = sut.getBalance(-1);

        Assert.assertEquals(0, testList.size());
    }

    @Test
    public void getBalance_given_validID_returns_correct_list(){
        List<BigDecimal> testList = sut.getBalance(USER_1.getId());

        Assert.assertEquals(3, testList.size());
        Assert.assertEquals(ACCOUNT_1.getBalance(), testList.get(0));
        Assert.assertEquals(ACCOUNT_3.getBalance(), testList.get(1));
        Assert.assertEquals(ACCOUNT_4.getBalance(), testList.get(2));
    }

    @Test
    public void getBalanceByAccountID_given_invalidId_returns_null(){
        BigDecimal number = sut.getBalanceByAccountID(-1);

        Assert.assertNull(number);
    }

    @Test
    public void getBalanceByAccountID_returns_correct_balance(){
        BigDecimal number = sut.getBalanceByAccountID(ACCOUNT_1.getAccount_id());

        Assert.assertEquals(ACCOUNT_1.getBalance(), number);
    }

    @Test
    public void getAccountsByUserID_returns_empty_given_invalidID(){
        List<Account> test = sut.getAccountsByUserID(-1);

        Assert.assertEquals(0, test.size());
    }

    @Test
    public void getAccountsByUserID_returns_accounts_given_validID(){
        List<Account> test = sut.getAccountsByUserID(USER_1.getId());

        Assert.assertEquals(3, test.size());
        Assert.assertEquals(ACCOUNT_1.getAccount_id(), test.get(0).getAccount_id());
        Assert.assertEquals(ACCOUNT_3.getAccount_id(), test.get(1).getAccount_id());
        Assert.assertEquals(ACCOUNT_4.getAccount_id(), test.get(2).getAccount_id());
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
            sut.checkAndUpdateBalance(new BigDecimal(100.00), ACCOUNT_1.getAccount_id(), ACCOUNT_2.getAccount_id());
        } catch (InsufficientFundsException e){
            System.err.println("Welp. Something went wrong");
        }
        Assert.assertEquals(new BigDecimal("900.00"), sut.getBalanceByAccountID(ACCOUNT_1.getAccount_id()));
        Assert.assertEquals(new BigDecimal("2100.00"), sut.getBalanceByAccountID(ACCOUNT_2.getAccount_id()));
    }


}
