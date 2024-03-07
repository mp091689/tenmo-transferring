package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {
    private final TransferDao transferDao;
    private final TransferService transferService;

    private final UserDao userDao;


    public TransferController(TransferDao transferDao, TransferService transferService, UserDao userDao) {
        this.transferDao = transferDao;
        this.transferService = transferService;
        this.userDao = userDao;
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

    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfer getById(@PathVariable int id, @RequestParam int userId){
        Transfer transfer = transferDao.getById(id, userId);
        if (transfer != null) {
            return transfer;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getAll(Principal p){
        User user = userDao.getUserByUsername(p.getName());
        return transferDao.getAll(user.getId());
    }
    @RequestMapping(path = "transfers/pending", method = RequestMethod.GET)
    public List<Transfer> getAllPending(int userId){
        return transferDao.getAllPending(userId);
    }

    @PutMapping("approve/{id}")
    public boolean approve(@PathVariable int id, @RequestBody String status, Principal principal) {
        return transferService.approve(id, status, principal.getName());
    }
}
