package main.account.infrastructure.controller;

import main.account.application.AccountSearcher;
import main.account.application.TransactionService;
import main.account.domain.Account;
import main.account.domain.AccountNotFound;
import main.account.domain.Movement;

import java.util.List;

public final class CliController {
    private final AccountSearcher accountSearcher;
    private final TransactionService transactionService;

    public CliController(AccountSearcher accountSearcher, TransactionService transactionService) {
        this.accountSearcher = accountSearcher;
        this.transactionService = transactionService;
    }

    public List<Account> getAllAccounts() {
        return this.accountSearcher.getAllAccounts();
    }

    public List<Movement> getAllMovements(String uuid) {
        return this.accountSearcher.getAllMovements(uuid);
    }

    public Account getAccount(String uuid) {
        try {
            return this.accountSearcher.getAccount(uuid);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }
    }

    public void transfer(String origin, String destination, Double amount) {
        try {
            this.transactionService.transfer(origin, destination, amount);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }
    }

    public void deposit(String account, Double amount) {
        try {
            this.transactionService.deposit(account, amount);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }
    }

    public void withdraw(String account, Double amount) {
        try {
            this.transactionService.withdraw(account, amount);
        } catch (AccountNotFound e) {
            throw new RuntimeException(e);
        }
    }
}
