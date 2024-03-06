package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private final String TRANSFER_SELECT = "SELECT t.transfer_id, t.transfer_status_id, t.transfer_status_id, t.account_from, t.account_to, t.amount FROM transfer AS t";
    @Override
    public List<Transfer> getAll(int userId) {
        //TODO: getAll
        List<Transfer> transfers = new ArrayList<>();
        String sql = TRANSFER_SELECT +
                "WHERE account_from IN (SELECT account_id FROM account WHERE user_id = ?) OR account_to IN (SELECT account_id FROM account WHERE user_id = ?)";
        return null;
    }

    @Override
    public Transfer getById(int id, int userId) {
        //TODO: getById
        return null;
    }

    @Override
    public Transfer create(Transfer transfer) {
        //TODO: create
        return null;
    }

    @Override
    public List<Transfer> getAllPending(int userId) {
        //TODO: getAllPending
        return null;
    }

    @Override
    public Boolean approve(int id, String status) {
        //TODO: approve
        return null;
    }

    private Transfer mapRowsToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
    }
}
