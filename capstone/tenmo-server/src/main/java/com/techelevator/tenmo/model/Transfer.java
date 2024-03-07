package com.techelevator.tenmo.model;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class Transfer {
    private int id;
    @Min(value = 1, message = "The field 'typeId' is required.")
    @Max(value = 2, message = "The field 'typeId' may not exceed a value of 2.")
    private int typeId;
    @Min(value = 1, message = "The field 'statusId' is required.")
    @Max(value = 3, message = "The field 'statusId' may not exceed a value of 3.")
    private int statusId;
    @Min(value = 1, message = "The field 'fromAccount' is required.")
    private int fromAccount;
    @Min(value = 1, message = "The field 'toAccount' is required.")
    private int toAccount;
    @DecimalMin(value = "0.01", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    private BigDecimal amount;

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
                "\n Transfer Type:'" + typeId + '\'' +
                "\n Transfer Status: " + statusId +
                "\n From: " + fromAccount +
                "\n To: " + toAccount +
                "\n Amount:" + amount;
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
    
}
