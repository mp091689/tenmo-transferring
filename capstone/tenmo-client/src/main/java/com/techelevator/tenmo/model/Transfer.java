package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int id;
    private int typeId;
    private int statusId;
    private int fromAccount;
    private int toAccount;
    private BigDecimal amount;
    private String userFrom;
    private String userTo;
    private String type;
    private String status;

    public Transfer(int id, int typeId, int statusId, int fromAccount, int toAccount, BigDecimal amount) {
        this.id = id;
        this.typeId = typeId;
        this.statusId = statusId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public Transfer() {

    }

    @Override
    public String toString() {
        return "\n--------------------------------------------" +
                "\n Transfer Details" +
                "\n--------------------------------------------" +
                "\n Id: " + id +
                "\n Transfer Type: " + typeId + '\'' +
                "\n Transfer Status: " + statusId +
                "\n From: " + fromAccount +
                "\n To: " + toAccount +
                "\n Amount: $" + amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(int fromAccount) {
        this.fromAccount = fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public void setToAccount(int toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
