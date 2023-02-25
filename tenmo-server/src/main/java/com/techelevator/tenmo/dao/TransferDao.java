package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getAllTransfers();
    List<Transfer> getAllTransfersForUser(int userId);

    Transfer getTransferById(int transferId);

    Transfer createTransfer(Transfer transfer);

    List<Transfer> getPendingTransfersByUserId(int userId);


    void updateTransfer(Transfer transfer);


}
