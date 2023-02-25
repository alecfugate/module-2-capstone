package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TransferDao transferDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping(path="/transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfersForUser(@PathVariable int id) {
        return transferDao.getAllTransfersForUser(id);
    }

    @RequestMapping(path="/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable int id) {
        return transferDao.getTransferById(id);
    }

    @RequestMapping(path="/transfers/user/{userId}", method = RequestMethod.GET)
    public List<Transfer> getTransferByUserId(@PathVariable int userId) {
        return transferDao.getAllTransfersForUser(userId);
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

    @RequestMapping(path="/transfers/user/{userId}/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfersByUserId(@PathVariable int userId) {
        return transferDao.getPendingTransfersByUserId(userId);
    }

    @RequestMapping(path="/transfers/{id}", method = RequestMethod.PUT)
    public void updateTransferStatus(@RequestBody Transfer transfer, @PathVariable int id) throws InsufficientFundsException {

        if(transfer.getTransferStatusId() == 2) {
            // only update balance if it is approved
            // check balance from account and update BALANCE for both account
            accountDao.checkAndUpdateBalance(transfer.getAmount(), transfer.getAccountFrom(), transfer.getAccountTo());
        }
        // update transfer status APPROVED or REJECTED
        transferDao.updateTransfer(transfer);

    }

}
