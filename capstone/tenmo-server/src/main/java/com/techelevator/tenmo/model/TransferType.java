package com.techelevator.tenmo.model;

public enum TransferType {
    REQUEST(1), SEND(2);

    public final int id;

    TransferType(int id) {
        this.id = id;
    }

    public static TransferType get(int id) {
        for (TransferType v : values()) {
            if (v.id == id) {
                return v;
            }
        }
        return null;
    }
}
