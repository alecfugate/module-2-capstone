package com.techelevator.tenmo.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTransferDaoTests extends BaseDaoTests{

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAllTransfersForUser_given_null_throws_exception(){sut.getAllTransfersForUser((Integer)null);}
}
