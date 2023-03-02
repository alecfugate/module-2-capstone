package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestingDatabaseConfig.class)
public abstract class BaseDaoTests {

    protected static final User USER_1 = new User(1001, "user1", "user1", "USER");
    protected static final User USER_2 = new User(1002, "user2", "user2", "USER");
    protected static final User USER_3 = new User(1003, "user3", "user3", "USER");


    protected static final Account ACCOUNT_1 = new Account(2001, USER_1.getId(), new BigDecimal("1000.00"));
    protected static final Account ACCOUNT_2 = new Account(2002, USER_2.getId(), new BigDecimal("2000.00"));
    protected static final Account ACCOUNT_3 = new Account(2003, USER_1.getId(), new BigDecimal("500.00"));
    protected static final Account ACCOUNT_4 = new Account(2004, USER_1.getId(), new BigDecimal("750.00"));

    @Autowired
    protected DataSource dataSource;

    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

}
