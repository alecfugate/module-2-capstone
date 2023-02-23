package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Transfer> getAllTransfersForUser(Integer userId) {
        List<Transfer> listOfTransfers = new ArrayList<>();

        String sqlGetTransfers = "select * from tenmo_user as tu " +
                "JOIN account ON tu.user_id = account.user_id " +
                "JOIN transfers as tran ON accounts.account_id = tran.account_from OR accounts.account_id = tran.account_to " +
                "WHERE account.account_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransfers, convertedAccountID(userId));
        while (results.next()) {
            Transfer transferHistory = mapRowToTransfer(results);
            listOfTransfers.add(transferHistory);
        }
        return listOfTransfers;

    }

    @Override
    public Transfer getTransferById(int transferId){
        String sql = "SELECT * from transfer" +
                "WHERE transfer_id = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
        return mapRowToTransfer(result);
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, transfer.getTransferTypeId());
            ps.setInt(2, transfer.getTransferStatusId());
            ps.setInt(3, transfer.getAccountFrom());
            ps.setInt(4, transfer.getAccountTo());
            ps.setBigDecimal(5, transfer.getAmount());
            return ps;
        }, keyHolder);

        int transferId = keyHolder.getKey().intValue();
        transfer.setTransferId(transferId);

        return transfer;
    }

    @Override
    public void updateTransfer(Transfer transfer) {
        String sql = "UPDATE transfer " +
                "SET transfer_status_id = ? " +
                "WHERE transfer_id = ?";

        jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
    }

    public int convertedAccountID(int userId) {
        int accountIdConverted = 0;
        String sqlConvertUserIdToAccountId = "SELECT account_id FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlConvertUserIdToAccountId, userId);
        while (results.next()) {
            accountIdConverted = results.getInt("account_id");
        }
        return accountIdConverted;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }

}
