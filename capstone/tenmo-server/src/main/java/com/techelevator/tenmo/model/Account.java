package com.techelevator.tenmo.model;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account implements RowMapper<Account> {
    private int id;
    private int userId;
    private BigDecimal balance;

    public Account() {
    }

    public Account(int id, int userId, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal deposit(BigDecimal amount) {
        balance = balance.add(amount);
        return balance;
    }

    public BigDecimal withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            return balance;
        }

        balance = balance.subtract(amount);
        return balance;
    }

    @Override
    public Account mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Account(
                resultSet.getInt("account_id"),
                resultSet.getInt("user_id"),
                resultSet.getBigDecimal("balance")
        );
    }
}
