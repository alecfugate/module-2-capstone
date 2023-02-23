package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getAllTransfers(int userId);

    List<Transfer> getAllTransfersByUserId(int userId);

    Transfer getTransferById(Long transferId);

    Transfer createTransfer(Transfer transfer);

    void updateTransfer(Transfer transfer);


}
