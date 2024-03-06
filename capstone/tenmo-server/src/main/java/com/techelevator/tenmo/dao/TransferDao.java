package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> getAll();
    Transfer getById(int id);
    Transfer create(Transfer transfer);
    List<Transfer> getAllPending();
    Boolean approve(int id, String status);

}
