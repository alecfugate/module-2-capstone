package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getAllTransfersForUser(Integer userId);

    Transfer getTransferById(int transferId);

    Transfer createTransfer(Transfer transfer);

    void updateTransfer(Transfer transfer);


}
