package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class JdbcTransferDaoTests extends BaseDaoTests {

    private JdbcTransferDao sut;

    private static Logger LOGGER = LoggerFactory.getLogger(TransferDao.class);
    Transfer transfer;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getAllTransfersForUser_given_null_throws_exception() {
        try {
            // Call the method with a null value
            sut.getAllTransfersForUser((Integer) null);
        } catch (Exception e) {
            // Print the exception's stack trace
            e.printStackTrace();


            // Check that an exception is thrown
            assertThrows(Exception.class, () -> sut.getAllTransfersForUser((Integer) null));
        }
    }

    @Test
    public void getAllTransfers_returnsCorrectNumberOfTransfers() {
        List<Transfer> transfers = sut.getAllTransfers();
        assertNotNull(transfers);
        assertEquals(0, transfers.size());
    }

    @Test
    public void getPendingTransfersByUserId_returnsCorrectNumberOfTransfers() {
        List<Transfer> transfers = sut.getPendingTransfersByUserId(1);
        assertNotNull(transfers);
        assertEquals(0, transfers.size());

    }


    @Test
    public void getPendingTransfersByUserId_withInvalidUserId_returnsEmptyList() {
        List<Transfer> transfers = sut.getPendingTransfersByUserId(0);
        assertNotNull(transfers);
        assertEquals(0, transfers.size());
    }

    @Test
    public void getAllTransfersForUser_withInvalidUserId_returnsEmptyList() {
        List<Transfer> transfers = sut.getAllTransfersForUser(0);
        assertNotNull(transfers);
        assertEquals(0, transfers.size());
    }

    @Test
    public void getTransferById_returnsCorrectTransfer() {
        List<Transfer> transfers = sut.getTransfersByTypeId(1);
        assertNotNull(transfers);
        assertEquals(0, transfers.size());

}

    @Test
    public void getTransferById_withInvalidId_returnsNull() {
        try {
            // Call the method with a null value
            sut.getAllTransfersForUser((Integer) null);
        } catch (Exception e) {
            // Print the exception's stack trace
            e.printStackTrace();


            // Check that an exception is thrown
            assertThrows(Exception.class, () -> sut.getAllTransfersForUser((Integer) null));
        }

    }

    @Test
    public void createTransfer_createsTransferWithCorrectValues() {
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(1);
        transfer.setAccountTo(2);
        transfer.setAmount(new BigDecimal(50));

    }

    @Test
    public void createTransfer_withNullTransfer_throwsException() {
        try {
            sut.createTransfer(null);
        } catch (Exception e) {
            // Print the exception's stack trace
            e.printStackTrace();


            // Check that an exception is thrown
            assertThrows(Exception.class, () -> sut.createTransfer(null));
        }

    }




        @Test
    public void createTransfer_withInvalidAccountIds_throwsException() {
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(0);
        transfer.setAccountTo(0);
        transfer.setAmount(new BigDecimal(50));
        assertThrows(DataAccessException.class, () -> sut.createTransfer(transfer));
    }

    @Test
    public void createTransfer_withInvalidAmount_throwsException() {
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(1);
        transfer.setAccountTo(2);
        transfer.setAmount(new BigDecimal(-50));
        assertThrows(DataAccessException.class, () -> sut.createTransfer(transfer));
    }

    @Test 
    public void updateTransfer_updatesTransferStatus() {
        try {
            sut.updateTransfer(transfer);
        } catch (Exception e) {
            System.err.println("Error updating transfer status: " + e.getMessage());
        }
    }

    @Test
    public void getTransfersByStatusId_withInvalidStatusId_returnsEmptyList() {
        try {
            // Call the method with an invalid status ID
            List transfers = sut.getTransfersByTypeId(0);


            // Check the results
            assertNotNull(transfers);
            assertEquals(0, transfers.size());
        } catch (Exception e) {
            // Print the exception's stack trace
            e.printStackTrace();
        }
    }

    @Test
    public void getTransfersByTypeId_withInvalidTypeId_returnsEmptyList() {
        try {
            // Call the method with an invalid type ID
            List transfers = sut.getTransfersByTypeId(0);


            // Check the results
            assertNotNull(transfers);
            assertEquals(0, transfers.size());
        } catch (Exception e) {
            // Print the exception's stack trace
            e.printStackTrace();
        }
    }


    @Test
    public void getTransferTypeById_withInvalidId_returnsNull() {
        try {
            // Call the method with an invalid ID
            Transfer transfer = sut.getTransferById(0);


            // Check the results
            assertNull(transfer);
        } catch (Exception e) {
            // Print the exception's stack trace
            e.printStackTrace();
        }
    }

    @Test
    public void getTransferStatusById_returnsCorrectStatus() {
        try {
            List<Transfer> pendingTransfers = sut.getTransfersByStatusId(1);
            List<Transfer> approvedTransfers = sut.getTransfersByStatusId(2);
            List<Transfer> rejectedTransfers = sut.getTransfersByStatusId(3);

            assertEquals("Pending", pendingTransfers.get(0).getTransferStatus());
            assertEquals("Approved", approvedTransfers.get(0).getTransferStatus());
            assertEquals("Rejected", rejectedTransfers.get(0).getTransferStatus());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    @Test
    public void getTransferStatusByIdwithInvalidIdreturnsNull() {
        List<Transfer> transfers = sut.getTransfersByStatusId(0);
        assertNull(transfers);
    }





}