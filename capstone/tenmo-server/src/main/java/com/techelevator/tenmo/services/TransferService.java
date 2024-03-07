package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransferService {
    private final TransferDao transferDao;
    private final AccountDao accountDao;
    private final UserDao userDao;

    public TransferService(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    public boolean approve(int transferId, String status, String userName) {
        User userFrom = userDao.getUserByUsername(userName);
        BigDecimal currentBalance = accountDao.getBalance(userFrom.getId());
        Transfer transfer = transferDao.getById(transferId, userFrom.getId());

        int statusId = status.equals("approve") ? 2 : 3;

        if (currentBalance.compareTo(transfer.getAmount()) < 0) {
            return false;
        }

        // TODO: decrease increase account balances

        return true;
    }

}
