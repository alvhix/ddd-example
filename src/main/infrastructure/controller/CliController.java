package main.infrastructure.controller;

import main.application.AccountSearcher;
import main.application.TransferenceService;
import main.domain.Account;
import main.domain.Movement;

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

    public void transfer(String origin, String destination, Double quantity) {
        this.transferenceService.transfer(origin, destination, quantity);
    }

    public Double deposit(String account, Double quantity) {
        return this.transferenceService.deposit(account, quantity);
    }

    public Double withdraw(String account, Double quantity) {
        return this.transferenceService.withdraw(account, quantity);
    }
}
