package com.techelevator.tenmo.model;

public enum TransferStatus {
    PENDING(1), APPROVED(2), DECLINED(3);

    public final int id;

    TransferStatus(int id) {
        this.id = id;
    }

    public static TransferStatus get(int id) {
        for (TransferStatus v : values()) {
            if (v.id == id) {
                return v;
            }
        }
        return null;
    }
}
