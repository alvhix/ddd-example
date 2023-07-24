package main.account.infrastructure.controller;

import main.account.application.AccountSearcher;
import main.account.application.TransferenceService;
import main.account.domain.Account;
import main.account.domain.Movement;

import java.util.List;

public final class CliController {
    private final AccountSearcher accountSearcher;
    private final TransferenceService transferenceService;

    public CliController(AccountSearcher accountSearcher, TransferenceService transferenceService) {
        this.accountSearcher = accountSearcher;
        this.transferenceService = transferenceService;
    }

    public List<Account> getAllAccounts() {
        return this.accountSearcher.getAllAccounts();
    }

    public List<Movement> getAllMovements(String uuid) {
        return this.accountSearcher.getAllMovements(uuid);
    }

    public void transfer(String origin, String destination, Double amount) {
        this.transferenceService.transfer(origin, destination, amount);
    }

    public Double deposit(String account, Double amount) {
        return this.transferenceService.deposit(account, amount);
    }

    public Double withdraw(String account, Double amount) {
        return this.transferenceService.withdraw(account, amount);
    }
}
