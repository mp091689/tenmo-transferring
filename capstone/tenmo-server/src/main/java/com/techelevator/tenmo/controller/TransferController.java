package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthorized()")
@RestController
public class TransferController {
    private Transfer transfer;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Transfer create(){
        Transfer newTransfer = null;
        return newTransfer;
    }
}
