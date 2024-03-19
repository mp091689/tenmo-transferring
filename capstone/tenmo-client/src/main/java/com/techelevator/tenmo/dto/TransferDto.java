package com.techelevator.tenmo.dto;

import com.techelevator.tenmo.model.TransferType;

import java.math.BigDecimal;

public class TransferDto {

    private int userId;

    private TransferType type;

    private BigDecimal amount;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.amount = amount;
        }
    }

    public TransferType getType() {
        return type;
    }

    public void setType(TransferType type) {
        this.type = type;
    }
}
