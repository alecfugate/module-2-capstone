package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TransferDao transferDao;

    @RequestMapping(path="/history/{id}", method = RequestMethod.GET)
    public List<Transfer> getAllTransfersForUser(@PathVariable int id) {
        return transferDao.getAllTransfersForUser(id);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path="/transfers/{id}", method = RequestMethod.POST)
    public void addTransfer(@RequestBody Transfer transfer, @PathVariable int id) throws InsufficientFundsException {

        if(transfer.getTransferStatusId()==2) {
            // only update balance if it is approved
            // check balance from account and update BALANCE for both account
            accountDao.checkAndUpdateBalance(transfer.getAmount(), transfer.getAccountFrom(), transfer.getAccountTo());
        }
        transferDao.createTransfer(transfer);

    }


}
