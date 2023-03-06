package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDao accountDao;

    private Account[] accounts = new Account[]{new Account(2001, 1001, new BigDecimal("1000.00"))};
    private String accountAsJSON = "[{\"accountId\":2001,\"userId\":1001,\"balance\":1000.00}]";

    @Test
    @WithMockUser
    public void getBalance_User_Not_Found_Returns_4XX() throws Exception {
        mockMvc.perform(get("/account/accounts"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "user1")
    public void getBalance_Returns_2XX_Given_Valid_User() throws Exception {


        when(accountDao.getBalance(1001)).thenReturn(accounts);
        mockMvc.perform(get("/account/accounts"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(equalTo(accountAsJSON)));
    }

    @Test
    public void getAccountsByUserID_Returns_2XX() throws Exception {
        when(accountDao.getAccountsByUserID(1001)).thenReturn(accounts);

        mockMvc.perform(get("/account/user/1001"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(equalTo(accountAsJSON)));
    }

    @Test
    public void getAccountsByUserID_Returns_Empty_Given_Invalid_UserID() throws Exception {
        mockMvc.perform(get("/account/user/1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(equalTo("")));
    }
}

