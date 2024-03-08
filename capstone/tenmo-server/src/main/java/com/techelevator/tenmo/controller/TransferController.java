package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferApproveDto;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("transfers")
public class TransferController {
    private final TransferDao transferDao;
    private final TransferService transferService;
    private final UserDao userDao;

    public TransferController(TransferDao transferDao, TransferService transferService, UserDao userDao) {
        this.transferDao = transferDao;
        this.transferService = transferService;
        this.userDao = userDao;
    }

    @GetMapping
    public List<Transfer> getAll(Principal principal) {
        User user = userDao.getUserByUsername(principal.getName());
        return transferDao.getAll(user.getId());
    }

    @GetMapping(path = "{id}")
    public Transfer getById(@PathVariable int id) {
        Transfer transfer = transferDao.getById(id);
        if (transfer != null) {
            return transfer;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
    }

    @GetMapping("pending")
    public List<Transfer> getAllPending(Principal principal) {
        User user = userDao.getUserByUsername(principal.getName());
        return transferDao.getAllPending(user.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer create(@Valid @RequestBody TransferDto transferDto, Principal principal) {
        User user = userDao.getUserByUsername(principal.getName());
        try {
            return transferService.create(transferDto, user.getId());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }
    }

    @PutMapping("{id}")
    public boolean approve(@PathVariable int id, @RequestBody TransferApproveDto transferApproveDto, Principal principal) {
        return transferService.approve(id, transferApproveDto.getStatusId(), principal.getName());
    }
}
