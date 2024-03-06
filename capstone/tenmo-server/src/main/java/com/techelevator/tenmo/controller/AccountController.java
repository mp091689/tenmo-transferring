package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
public class AccountController {
    private AccountDao accountDao;
    
    @GetMapping("balance")
    public BigDecimal getBalance(@Valid @RequestParam int userId) {
        return accountDao.getBalance(userId);
    }
}
