package com.techelevator.tenmo.model;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account implements RowMapper<Account> {
    private int id;
    private User user;
    private BigDecimal balance;

    public Account() {
    }

    public Account(int id, User user, BigDecimal balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("username"));

        return new Account(
                resultSet.getInt("account_id"),
                user,
                resultSet.getBigDecimal("balance")
        );
    }
}
