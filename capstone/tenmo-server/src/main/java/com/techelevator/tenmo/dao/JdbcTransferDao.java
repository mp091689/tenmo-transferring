package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public class JdbcTransferDao implements TransferDao{
    @Override
    public List<Transfer> getAll() {
        return null;
    }

    @Override
    public Transfer getById(int id) {
        return null;
    }

    @Override
    public Transfer create(Transfer transfer) {
        return null;
    }

    @Override
    public List<Transfer> getAllPending() {
        return null;
    }

    @Override
    public Boolean approve(int id, String status) {
        return null;
    }
}
