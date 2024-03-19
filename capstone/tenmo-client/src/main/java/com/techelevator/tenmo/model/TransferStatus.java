package com.techelevator.tenmo.model;

public enum TransferStatus {
    PENDING, APPROVED, DECLINED;

    @Override
    public String toString() {
        String name = super.toString();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
