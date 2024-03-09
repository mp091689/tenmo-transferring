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
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING *";
        Object[] args = {transfer.getTypeId(), transfer.getStatusId(), transfer.getFromAccount(), transfer.getToAccount(), transfer.getAmount()};
        try {
            return jdbcTemplate.queryForObject(sql, new Transfer(), args);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public List<Transfer> getAllPending(int userId) {
        String sql = TRANSFER_SELECT + "WHERE (t.account_from IN (SELECT account_id FROM account WHERE user_id = ?) OR t.account_to IN (SELECT account_id FROM account WHERE user_id = ?)) AND transfer_status_id = 1";
        try {
            return jdbcTemplate.query(sql, new Transfer(), userId, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to database.", e);
        }
    }

    @Override
    public Transfer update(Transfer transfer) {
        try {
            String sql = "UPDATE transfer t " +
                    "SET transfer_status_id = ? " +
                    "WHERE t.transfer_type_id = 1 " +
                    "AND t.transfer_status_id = 1 " +
                    "AND t.transfer_id = ? " +
                    "AND t.account_to = ?";
            jdbcTemplate.update(sql, transfer.getStatusId(), transfer.getId(), transfer.getToAccount());
            return getById(transfer.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    // TODO get everything
    // I will use this to improve the print out in client
//    SELECT t.transfer_id, t.transfer_type_id, tt.transfer_type_desc, t.transfer_status_id, ts.transfer_status_desc, t.account_from, af.username AS from_username, t.account_to, ato.username AS to_username, t.amount
//    FROM transfer AS t
//    JOIN transfer_type AS tt ON t.transfer_type_id = tt.transfer_type_id
//    JOIN transfer_status AS ts ON t.transfer_status_id = ts.transfer_status_id
//    JOIN account AS afrom ON t.account_from = afrom.account_id
//    JOIN tenmo_user AS af ON afrom.user_id = af.user_id
//    JOIN account AS atoid ON t.account_to = atoid.account_id
//    JOIN tenmo_user AS ato ON atoid.user_id = ato.user_id;
}
