package com.techelevator.tenmo.controller;

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Transfer create(){
        Transfer newTransfer = null;
        return newTransfer;
    }

    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public boolean getById(@Valid @PathVariable int id){
        return true;
    }
}
