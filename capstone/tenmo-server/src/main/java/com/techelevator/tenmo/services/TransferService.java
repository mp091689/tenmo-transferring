package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;

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
        User user = userDao.getUserByUsername(userName);
        Transfer transfer = transferDao.getById(transferId, user.getId());
        Account accountFrom = accountDao.getById(transfer.getFromAccount());

        int statusId = status.equals("approve") ? 2 : 3;

        if (accountFrom.getBalance().compareTo(transfer.getAmount()) < 0) {
            return false;
        }

        if (transferDao.approve(transferId, statusId, user.getId()) < 1) {
            return false;
        }

        Account accountTo = accountDao.getById(transfer.getToAccount());
        accountTo.deposit(transfer.getAmount());
        accountFrom.withdraw(transfer.getAmount());

        return true;
    }

    public Transfer create(TransferDto transferDto, int userId) {
        Account currentAccount = accountDao.getByUserId(userId);
        Account foreignAccount = accountDao.getByUserId(transferDto.getUserId());

        Transfer transfer = new Transfer();
        transfer.setAmount(transferDto.getAmount());
        transfer.setTypeId(transferDto.getTypeId());
        transfer.setStatusId(1);

        if (transferDto.getTypeId() == 2) { // sending
            transfer.setFromAccount(currentAccount.getId());
            transfer.setToAccount(foreignAccount.getId());
            if (currentAccount.getBalance().compareTo(transfer.getAmount()) >= 0) {
                foreignAccount.deposit(transfer.getAmount());
                currentAccount.withdraw(transfer.getAmount());
                transfer.setStatusId(2);
            }
            transfer.setStatusId(3);
        } else { // requesting
            transfer.setFromAccount(foreignAccount.getId());
            transfer.setToAccount(currentAccount.getId());
        }

        return transferDao.create(transfer, userId);
    }
}
