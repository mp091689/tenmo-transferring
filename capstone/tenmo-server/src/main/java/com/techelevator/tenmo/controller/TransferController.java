package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

// We need something like this
//@PreAuthorize("isAuthorized()")
@RestController
public class TransferController {
    private Transfer transfer;

    private TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Transfer create(){
        Transfer newTransfer = null;
        return newTransfer;
    }

    @GetMapping(path = "/transfers/{id}")
    public boolean getById(@Valid @PathVariable int id){
        return true;
    }

    @PutMapping("approve/{id}")
    public boolean approve(@PathVariable int id, @RequestBody String status) {
        return transferDao.approve(id, status);
    }
}
