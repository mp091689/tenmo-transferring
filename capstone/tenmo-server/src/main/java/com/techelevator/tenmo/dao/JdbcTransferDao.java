package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private final String TRANSFER_SELECT = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount FROM transfer AS t ";

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Transfer> getAll(int userId) {
        String sql = TRANSFER_SELECT + "WHERE t.account_from IN (SELECT account_id FROM account WHERE user_id = ?) OR t.account_to IN (SELECT account_id FROM account WHERE user_id = ?)";
        try {
            return jdbcTemplate.query(sql, new Transfer(), userId, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to database.", e);
        }
    }

    @Override
    public Transfer getById(int id) {
        String sql = TRANSFER_SELECT + "WHERE t.transfer_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Transfer(), id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to database.", e);
        }
    }

    @Override
    public Transfer create(Transfer transfer, int userId) {
        Transfer newTransfer;
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
        String sql = TRANSFER_SELECT + "WHERE t.account_from IN (SELECT account_id FROM account WHERE user_id = ?) OR t.account_to IN (SELECT account_id FROM account WHERE user_id = ?) AND transfer_status_id = 1;";
        try {
            return jdbcTemplate.query(sql, new Transfer());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to database.", e);
        }
    }

    @Override
    public int approve(int transferId, int statusId, int userId) {
        try {
            String sql = "UPDATE transfer t " +
                    "SET transfer_status_id = ? " +
                    "WHERE t.transfer_type_id = 1 " +
                    "AND t.transfer_status_id = 1 " +
                    "AND t.transfer_id = ? " +
                    "AND t.account_to = ?";
            return jdbcTemplate.update(sql, statusId, transferId, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }
}
