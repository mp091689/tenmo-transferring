package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.dto.TransferDto;
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

    public Transfer approve(int transferId, int statusId, String userName) {
        User user = userDao.getUserByUsername(userName);
        Transfer transfer = transferDao.getById(transferId);
        Account accountFrom = accountDao.getById(transfer.getFromAccount().getId());

        Account accountTo = accountDao.getById(transfer.getToAccount().getId());
        if (accountTo.getUser().getId() == user.getId()){
            throw new RuntimeException("It is not possible to approve/decline transfer requested by you");
        }

        if (transfer.getStatus() != TransferStatus.PENDING) {
            throw new RuntimeException("The Transfer is already approved/declined");
        }

        if (statusId == 2 && accountFrom.getBalance().compareTo(transfer.getAmount()) < 0) {
            throw new RuntimeException("Not enough money on your account");
        }

        accountTo.deposit(transfer.getAmount());
        accountDao.update(accountTo);

        accountFrom.withdraw(transfer.getAmount());
        accountDao.update(accountFrom);

        transfer.setStatus(TransferStatus.get(statusId));
        return transferDao.update(transfer);
    }

    public Transfer create(TransferDto transferDto, int userId) {
        Account currentAccount = accountDao.getByUserId(userId);
        Account foreignAccount = accountDao.getByUserId(transferDto.getUserId());

        if (currentAccount.getId() == foreignAccount.getId()){
            throw new RuntimeException("It is not possible to send money to yourself");
        }

        Transfer transfer = new Transfer();
        transfer.setAmount(transferDto.getAmount());
        transfer.setType(transferDto.getType());
        transfer.setStatus(TransferStatus.get(1));

        if (transferDto.getType() == TransferType.SEND) { // sending
            transfer.setFromAccount(currentAccount);
            transfer.setToAccount(foreignAccount);
            if (currentAccount.getBalance().compareTo(transfer.getAmount()) >= 0) {
                foreignAccount.deposit(transfer.getAmount());
                accountDao.update(foreignAccount);
                currentAccount.withdraw(transfer.getAmount());
                accountDao.update(currentAccount);
                transfer.setStatus(TransferStatus.get(2));
            } else {
                transfer.setStatus(TransferStatus.get(3));
            }
        } else { // requesting
            transfer.setFromAccount(foreignAccount);
            transfer.setToAccount(currentAccount);
        }

        transfer = transferDao.create(transfer, userId);

        if (transfer.getStatus() == TransferStatus.DECLINED) {
            throw new RuntimeException("Not enough money on your account");
        }

        return transfer;
    }
}
