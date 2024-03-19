package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int id;
    private TransferType type;
    private TransferStatus status;
    private Account fromAccount;
    private Account toAccount;
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

}
