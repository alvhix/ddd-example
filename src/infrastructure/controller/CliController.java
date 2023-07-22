package infrastructure.controller;

import application.AccountSearcher;
import application.TransferenceService;
import domain.Account;
import domain.Movement;

import java.util.List;

public final class CliController {
    private final AccountSearcher accountSearcher;
    private final TransferenceService transferenceService;

    public CliController(AccountSearcher accountSearcher, TransferenceService transferenceService) {
        this.accountSearcher = accountSearcher;
        this.transferenceService = transferenceService;
    }

    public List<Account> all() {
        return this.accountSearcher.getAllAccounts();
    }

    public List<Movement> getAllMovements(String uuid) {
        return this.accountSearcher.getAllMovements(uuid);
    }

    public void transfer(String origin, String destination, Double quantity) {
        this.transferenceService.transfer(origin, destination, quantity);
    }
}
