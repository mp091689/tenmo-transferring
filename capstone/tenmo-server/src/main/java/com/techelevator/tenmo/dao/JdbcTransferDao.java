package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.cache.annotation.CacheAnnotationParser;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private final String TRANSFER_SELECT = "SELECT t.transfer_id, t.transfer_status_id, t.transfer_status_id, t.account_from, t.account_to, t.amount FROM transfer AS t";

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Transfer> getAll(int userId) {
        //TODO: getAll
        List<Transfer> transfers = new ArrayList<>();
        String sql = TRANSFER_SELECT +
                "WHERE account_from IN (SELECT account_id FROM account WHERE user_id = ?) OR account_to IN (SELECT account_id FROM account WHERE user_id = ?)";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()){
                Transfer transferResult = mapRowsToTransfer(results);
                transfers.add(transferResult);
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to database.", e);
        }
        return transfers;
    }

    @Override
    public Transfer getById(int id, int userId) {
        //TODO: getById
        Transfer transfer = null;
        String sql = TRANSFER_SELECT +
                "WHERE transfer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()){
                transfer = mapRowsToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Cannot connect to database.", e);
        }
        return transfer;
    }

    @Override
    public Transfer create(Transfer transfer) {
        //TODO: create
        Transfer newTransfer = new Transfer();
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        try {
            int newTransferId = jdbcTemplate.queryForObject(sql, int.class, transfer.getTypeId(), transfer.getStatusId(), transfer.getFromAccount(), transfer.getToAccount(), transfer.getAmount());
            newTransfer = getById(newTransferId);
        } catch (CannotGetJdbcConnectionException e) {
        throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
        throw new DaoException("Data integrity violation", e);
    }
        return newTransfer;
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
        transfer.setId(results.getInt("transfer_id"));
        transfer.setTypeId(results.getInt("transfer_type_id"));
        transfer.setStatusId(results.getInt("transfer_status_id"));
        transfer.setFromAccount(results.getInt("account_from"));
        transfer.setToAccount(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}
