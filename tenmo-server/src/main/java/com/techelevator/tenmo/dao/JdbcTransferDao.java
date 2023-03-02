package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
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
    public List<Transfer> getAllTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            transfers.add(mapRowToTransfer(rowSet));
        }
        return transfers;
    }

    @Override
    public List<Transfer> getAllTransfersForUser(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount, " +
                "u_from.username AS from_username, u_to.username AS to_username " +
                "FROM transfer t " +
                "JOIN account a_from ON t.account_from = a_from.account_id " +
                "JOIN account a_to ON t.account_to = a_to.account_id " +
                "JOIN tenmo_user u_from ON a_from.user_id = u_from.user_id " +
                "JOIN tenmo_user u_to ON a_to.user_id = u_to.user_id " +
                "WHERE a_from.user_id = ? OR a_to.user_id = ? " +
                "ORDER BY t.transfer_id ASC";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getPendingTransfersByUserId(int userId) {


        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, " +
                "t.amount, tu_from.username AS from_username, tu_to.username AS to_username " +
                "FROM transfer t " +
                "JOIN account a_from ON t.account_from = a_from.account_id " +
                "JOIN account a_to ON t.account_to = a_to.account_id " +
                "JOIN tenmo_user tu_from ON a_from.user_id = tu_from.user_id " +
                "JOIN tenmo_user tu_to ON a_to.user_id = tu_to.user_id " +
                "WHERE t.transfer_status_id = 1 AND t.account_from IN " +
                "(SELECT account_id FROM account WHERE user_id = ?) " +
                "ORDER BY t.transfer_id ASC";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        List<Transfer> transfers = new ArrayList<>();
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
        return mapRowToTransfer(result);
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, transfer.getTransferTypeId());
            ps.setInt(2, transfer.getTransferStatusId());
            ps.setInt(3, transfer.getAccountFrom());
            ps.setInt(4, transfer.getAccountTo());
            ps.setBigDecimal(5, transfer.getAmount());
            return ps;
        });
//
//        int transferId = keyHolder.getKey().intValue();
//        transfer.setTransferId(transferId);

        return transfer;
    }

    @Override
    public void updateTransfer(Transfer transfer) {
        String sql = "UPDATE transfer " +
                "SET transfer_status_id = ? " +
                "WHERE transfer_id = ?";

        jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setUserTo(rowSet.getString("to_username"));
        transfer.setUserFrom(rowSet.getString("from_username"));
        return transfer;
    }

    public List getTransfersByStatusId(int statusId) {
        List transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE transferstatusid = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, statusId);
        while (rows.next()) {
            Transfer transfer = mapRowToTransfer(rows);
            transfers.add(transfer);

        }
        return transfers;
    }


    public List<Transfer> getTransfersByTypeId(int typeId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE transfer_type_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, typeId);
        while (rows.next()) {
            Transfer transfer = mapRowToTransfer(rows);
            transfers.add(transfer);
        }
        return transfers;
    }

    }
