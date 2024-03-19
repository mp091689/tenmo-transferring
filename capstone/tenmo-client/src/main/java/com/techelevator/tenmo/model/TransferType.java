package com.techelevator.tenmo.model;

public enum TransferType {
    REQUEST, SEND;

    @Override
    public String toString() {
        String name = super.toString();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
