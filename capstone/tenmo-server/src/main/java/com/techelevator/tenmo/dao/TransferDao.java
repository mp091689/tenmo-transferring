package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;
import java.util.List;

public interface TransferDao {
    List<Transfer> getAll(int userId);
    Transfer getById(int id, int userId);
    Transfer create(Transfer transfer);
    List<Transfer> getAllPending(int userId);
    int approve(int transferId, int statusId, int userId);
}
