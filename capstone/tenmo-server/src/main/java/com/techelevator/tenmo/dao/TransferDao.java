package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> getAll(int userId);
    Transfer getById(int id);
    Transfer create(Transfer transfer, int userId);
    List<Transfer> getAllPending(int userId);
    Transfer update(Transfer transfer);
}
