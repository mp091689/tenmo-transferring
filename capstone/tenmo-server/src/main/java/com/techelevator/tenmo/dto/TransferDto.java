package com.techelevator.tenmo.dto;

import com.techelevator.tenmo.model.TransferType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class TransferDto {

    @Min(value = 0L)
    private int userId;

    private TransferType type;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=13, fraction=2)
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
        this.amount = amount;
    }

    public TransferType getType() {
        return type;
    }

    public void setTypeId(TransferType type) {
        this.type = type;
    }
}
