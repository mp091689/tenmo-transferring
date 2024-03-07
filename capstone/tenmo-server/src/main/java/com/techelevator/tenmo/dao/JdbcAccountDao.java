package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int userID) {
        BigDecimal balance;
        String sql = "SELECT balance FROM account AS a " +
                "JOIN tenmo_user AS tu ON tu.user_id = a.user_id " +
                "WHERE a.user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userID);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            } else {
                throw new DaoException("Account not found");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return balance;
    }

    @Override
    public Account getById(int id) {
        Account account = null;

        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                account = map(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return account;
    }

    @Override
    public Account getByUserId(int userId) {
        Account account = null;

        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                account = map(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return account;
    }

    private Account map(SqlRowSet rs) {
       return new Account(
                rs.getInt("account_id"),
                rs.getInt("user_id"),
                rs.getBigDecimal("balance")
        );
    }
}
