package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
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
    @PostMapping("transfer")
    public Transfer create(@Valid @RequestBody Transfer transfer){
        Transfer newTransfer = null;
        try {
            newTransfer = transferDao.create(transfer);
        }
        catch (CannotGetJdbcConnectionException ex) {
            throw new DaoException("Connection error.", ex);
        }
        catch (DataIntegrityViolationException ex) {
            throw new DaoException("Error with data integrity.", ex);
        }
        catch (BadSqlGrammarException ex) {
            throw new DaoException("Please review your SQL string.", ex);
        }
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
