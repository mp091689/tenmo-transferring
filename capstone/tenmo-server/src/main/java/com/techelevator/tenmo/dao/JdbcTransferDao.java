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
    private final String SELECT_QUERY = "SELECT t.transfer_id, " +
            "       t.transfer_type_id, " +
            "       t.transfer_status_id, " +
            "       t.account_from, " +
            "       t.account_to, " +
            "       t.amount, " +
            "       from_a.account_id from_account_id, " +
            "       from_a.balance from_balance, " +
            "       from_u.user_id from_user_id, " +
            "       from_u.username from_username, " +
            "       to_a.account_id to_account_id, " +
            "       to_a.balance to_balance, " +
            "       to_u.user_id to_user_id, " +
            "       to_u.username to_username " +
            "FROM transfer t " +
            "         JOIN account from_a ON t.account_from = from_a.account_id" +
            "         JOIN account to_a ON t.account_to = to_a.account_id" +
            "         JOIN tenmo_user from_u ON from_a.user_id = from_u.user_id" +
            "         JOIN tenmo_user to_u ON to_a.user_id = to_u.user_id";

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Transfer> getAll(int userId) {
        String sql = SELECT_QUERY + " WHERE t.account_from IN (SELECT account_id FROM account WHERE user_id = ?) OR t.account_to IN (SELECT account_id FROM account WHERE user_id = ?)";
        try {
            return jdbcTemplate.query(sql, new Transfer(), userId, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to database.", e);
        }
    }

    @Override
    public Transfer getById(int id) {
        String sql = SELECT_QUERY + " WHERE t.transfer_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Transfer(), id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to database.", e);
        }
    }

    @Override
    public Transfer create(Transfer transfer, int userId) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        Object[] args = {transfer.getType().id, transfer.getStatus().id, transfer.getFromAccount().getId(), transfer.getToAccount().getId(), transfer.getAmount()};
        try {
            int transferId = jdbcTemplate.queryForObject(sql, int.class, args);
            return getById(transferId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public List<Transfer> getAllPending(int userId) {
        String sql = SELECT_QUERY + " WHERE (t.account_from IN (SELECT account_id FROM account WHERE user_id = ?) OR t.account_to IN (SELECT account_id FROM account WHERE user_id = ?)) AND transfer_status_id = 1";
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
            jdbcTemplate.update(sql, transfer.getStatus().id, transfer.getId(), transfer.getToAccount().getId());
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
