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

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/transfers")
public class TransferController {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TransferDao transferDao;

    @Autowired
    private UserDao userDao;

    @GetMapping
    public List<Transfer> getAllTransfersForUser(@PathVariable int id) {
        return transferDao.getAllTransfersForUser(id);
    }

    @GetMapping(path = "/{id}")
    public Transfer getTransferByTransferId(@PathVariable int id) {
        return transferDao.getTransferById(id);
    }

    @GetMapping(path = "/user/{userId}")
    public List<Transfer> getTransferByUserId(@PathVariable int userId) {
        return transferDao.getAllTransfersForUser(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/send_to/{id}")
    public void addTransfer(@RequestBody Transfer transfer, @PathVariable int id) throws InsufficientFundsException {

        if(transfer.getTransferStatusId()==2) {
            // only update balance if it is approved
            // check balance from account and update BALANCE for both account
            if (accountDao.checkBalance(transfer.getAmount(), transfer.getAccountFrom())) {
                accountDao.updateBalanceTransfer(transfer.getAmount(), transfer.getAccountFrom(), transfer.getAccountTo());
            }
        }
        transferDao.createTransfer(transfer);
    }

    @GetMapping(path = "/user/{userId}/pending")
    public List<Transfer> getPendingTransfersByUserId(@PathVariable int userId) {
        return transferDao.getPendingTransfersByUserId(userId);
    }

    @PutMapping(path = "update_status/{id}")
    public void updateTransferStatus(@RequestBody Transfer transfer, @PathVariable int id) throws InsufficientFundsException {

        // only update balance if it is approved
        if(transfer.getTransferStatusId() == 2) {

            // check balance from account and update BALANCE for both account
            if (accountDao.checkBalance(transfer.getAmount(), transfer.getAccountFrom())) {
                accountDao.updateBalanceTransfer(transfer.getAmount(), transfer.getAccountFrom(), transfer.getAccountTo());
            }
        }
        // update transfer status APPROVED or REJECTED
        transferDao.updateTransfer(transfer);

    }

}
