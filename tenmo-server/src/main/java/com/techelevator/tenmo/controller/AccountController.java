package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/account/")
public class AccountController {


    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping(path = "balance", method = RequestMethod.GET)
    public List<BigDecimal> getBalance(Principal principal) {
        return accountDao.getBalance(userDao.findIdByUsername(principal.getName()));

    }

    @RequestMapping(path = "user/{id}", method= RequestMethod.GET)
    public List<Account> getAccountByUserId(@PathVariable int id) {
        return accountDao.getAccountsByUserID(id);
    }

}
