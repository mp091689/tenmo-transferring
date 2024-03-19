package com.techelevator.tenmo.model;

import org.springframework.jdbc.core.RowMapper;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transfer implements RowMapper<Transfer> {
    private int id;
    private TransferType type;
    private TransferStatus status;
    private Account fromAccount;
    private Account toAccount;
    @DecimalMin(value = "0.01", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    private BigDecimal amount;

    public Transfer(int id, TransferType type, TransferStatus status, Account fromAccount, Account toAccount, BigDecimal amount) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public Transfer() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransferType getType() {
        return type;
    }

    public void setType(TransferType type) {
        this.type = type;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public Transfer mapRow(ResultSet resultSet, int i) throws SQLException {
        Transfer transfer = new Transfer();
        transfer.setId(resultSet.getInt("transfer_id"));
        transfer.setType(TransferType.get(resultSet.getInt("transfer_type_id")));
        transfer.setStatus(TransferStatus.get(resultSet.getInt("transfer_status_id")));
        transfer.setAmount(resultSet.getBigDecimal("amount"));

        User fromUser = new User(
                resultSet.getInt("from_user_id"),
                resultSet.getString("from_username")
        );
        Account fromAccount = new Account(
                resultSet.getInt("from_account_id"),
                fromUser,
                resultSet.getBigDecimal("from_balance")
        );
        transfer.setFromAccount(fromAccount);

        User toUser = new User(
                resultSet.getInt("to_user_id"),
                resultSet.getString("to_username")
        );
        Account toAccount = new Account(
                resultSet.getInt("to_account_id"),
                toUser,
                resultSet.getBigDecimal("to_balance")
        );
        transfer.setToAccount(toAccount);

        return transfer;
    }
}
